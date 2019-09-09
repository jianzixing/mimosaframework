package org.mimosaframework.core.json.parser.deserializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.parser.ParseContext;
import org.mimosaframework.core.json.util.FieldInfo;
import org.mimosaframework.core.json.ModelException;
import org.mimosaframework.core.json.parser.Feature;
import org.mimosaframework.core.json.parser.ModelToken;
import org.mimosaframework.core.json.parser.ParserConfig;

public class ArrayListTypeFieldDeserializer extends FieldDeserializer {

    private final Type         itemType;
    private int                itemFastMatchToken;
    private ObjectDeserializer deserializer;

    public ArrayListTypeFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo);

        Type fieldType = fieldInfo.fieldType;
        if (fieldType instanceof ParameterizedType) {
            this.itemType = ((ParameterizedType) fieldInfo.fieldType).getActualTypeArguments()[0];
        } else {
            this.itemType = Object.class;
        }
    }

    public int getFastMatchToken() {
        return ModelToken.LBRACKET;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void parseField(DefaultModelParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        if (parser.lexer.token() == ModelToken.NULL) {
            setValue(object, null);
            return;
        }

        ArrayList list = new ArrayList();

        ParseContext context = parser.getContext();

        parser.setContext(context, object, fieldInfo.name);
        parseArray(parser, objectType, list);
        parser.setContext(context);

        if (object == null) {
            fieldValues.put(fieldInfo.name, list);
        } else {
            setValue(object, list);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final void parseArray(DefaultModelParser parser, Type objectType, Collection array) {
        Type itemType = this.itemType;
        ObjectDeserializer itemTypeDeser = this.deserializer;
        
        if (itemType instanceof TypeVariable //
            && objectType instanceof ParameterizedType) {
            TypeVariable typeVar = (TypeVariable) itemType;
            ParameterizedType paramType = (ParameterizedType) objectType;

            Class<?> objectClass = null;
            if (paramType.getRawType() instanceof Class) {
                objectClass = (Class<?>) paramType.getRawType();
            }

            int paramIndex = -1;
            if (objectClass != null) {
                for (int i = 0, size = objectClass.getTypeParameters().length; i < size; ++i) {
                    TypeVariable item = objectClass.getTypeParameters()[i];
                    if (item.getName().equals(typeVar.getName())) {
                        paramIndex = i;
                        break;
                    }
                }
            }

            if (paramIndex != -1) {
                itemType = paramType.getActualTypeArguments()[paramIndex];
                if (!itemType.equals(this.itemType)) {
                    itemTypeDeser = parser.getConfig().getDeserializer(itemType);
                }
            }
        }

        final ModelLexer lexer = parser.lexer;

        if (lexer.token() != ModelToken.LBRACKET) {
            String errorMessage = "exepct '[', but " + ModelToken.name(lexer.token());
            if (objectType != null) {
                errorMessage += ", type : " + objectType;
            }
            throw new ModelException(errorMessage);
        }

        if (itemTypeDeser == null) {
            itemTypeDeser = deserializer = parser.getConfig().getDeserializer(itemType);
            itemFastMatchToken = deserializer.getFastMatchToken();
        }

        lexer.nextToken(itemFastMatchToken);

        for (int i = 0;; ++i) {
            if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                while (lexer.token() == ModelToken.COMMA) {
                    lexer.nextToken();
                    continue;
                }
            }

            if (lexer.token() == ModelToken.RBRACKET) {
                break;
            }

            Object val = itemTypeDeser.deserialze(parser, itemType, i);
            array.add(val);

            parser.checkListResolve(array);

            if (lexer.token() == ModelToken.COMMA) {
                lexer.nextToken(itemFastMatchToken);
                continue;
            }
        }

        lexer.nextToken(ModelToken.COMMA);
    }

}
