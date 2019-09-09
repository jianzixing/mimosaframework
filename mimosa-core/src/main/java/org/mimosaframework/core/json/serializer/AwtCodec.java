package org.mimosaframework.core.json.serializer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Type;

import org.mimosaframework.core.json.parser.DefaultModelParser;
import org.mimosaframework.core.json.parser.ModelLexer;
import org.mimosaframework.core.json.Model;
import org.mimosaframework.core.json.ModelException;
import org.mimosaframework.core.json.parser.ModelToken;
import org.mimosaframework.core.json.parser.deserializer.ObjectDeserializer;

public class AwtCodec implements ObjectSerializer, ObjectDeserializer {

    public final static AwtCodec instance = new AwtCodec();
    
    public static boolean support(Class<?> clazz) {
        return clazz == Point.class //
               || clazz == Rectangle.class //
               || clazz == Font.class //
               || clazz == Color.class //
        ;
    }

    public void write(ModelSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        char sep = '{';

        if (object instanceof Point) {
            Point font = (Point) object;
            
            sep = writeClassName(out, Point.class, sep);
            
            out.writeFieldValue(sep, "x", font.getX());
            out.writeFieldValue(',', "y", font.getY());
        } else if (object instanceof Font) {
            Font font = (Font) object;
            
            sep = writeClassName(out, Font.class, sep);
            
            out.writeFieldValue(sep, "name", font.getName());
            out.writeFieldValue(',', "style", font.getStyle());
            out.writeFieldValue(',', "size", font.getSize());
        } else if (object instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) object;
            
            sep = writeClassName(out, Rectangle.class, sep);
            
            out.writeFieldValue(sep, "x", rectangle.getX());
            out.writeFieldValue(',', "y", rectangle.getY());
            out.writeFieldValue(',', "width", rectangle.getWidth());
            out.writeFieldValue(',', "height", rectangle.getHeight());
        } else if (object instanceof Color) {
            Color color = (Color) object;
            
            sep = writeClassName(out, Color.class, sep);
            
            out.writeFieldValue(sep, "r", color.getRed());
            out.writeFieldValue(',', "g", color.getGreen());
            out.writeFieldValue(',', "b", color.getBlue());
            if (color.getAlpha() > 0) {
                out.writeFieldValue(',', "alpha", color.getAlpha());
            }
        } else {
            throw new ModelException("not support awt class : " + object.getClass().getName());
        }

        out.write('}');

    }

    protected char writeClassName(SerializeWriter out, Class<?> clazz, char sep) {
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            out.write('{');
            out.writeFieldName(Model.DEFAULT_TYPE_KEY);
            out.writeString(clazz.getName());
            sep = ',';
        }
        return sep;
    }

    @SuppressWarnings("unchecked")

    public <T> T deserialze(DefaultModelParser parser, Type type, Object fieldName) {
        ModelLexer lexer = parser.lexer;

        if (lexer.token() == ModelToken.NULL) {
            lexer.nextToken(ModelToken.COMMA);
            return null;
        }

        if (lexer.token() != ModelToken.LBRACE && lexer.token() != ModelToken.COMMA) {
            throw new ModelException("syntax error");
        }
        lexer.nextToken();
        
        if (type == Point.class) {
            return (T) parsePoint(parser);
        }
        
        if (type == Rectangle.class) {
            return (T) parseRectangle(parser);
        }
        
        if (type == Color.class) {
            return (T) parseColor(parser);
        }
        
        if (type == Font.class) {
            return (T) parseFont(parser);
        }

        throw new ModelException("not support awt class : " + type);
    }
    
    protected Font parseFont(DefaultModelParser parser) {
        ModelLexer lexer = parser.lexer;
        
        int size = 0, style = 0;
        String name = null;
        for (;;) {
            if (lexer.token() == ModelToken.RBRACE) {
                lexer.nextToken();
                break;
            }

            String key;
            if (lexer.token() == ModelToken.LITERAL_STRING) {
                key = lexer.stringVal();
                lexer.nextTokenWithColon(ModelToken.LITERAL_INT);
            } else {
                throw new ModelException("syntax error");
            }


            if (key.equalsIgnoreCase("name")) {
                if (lexer.token() == ModelToken.LITERAL_STRING) {
                    name = lexer.stringVal();
                    lexer.nextToken();
                } else {
                    throw new ModelException("syntax error");
                }
            } else if (key.equalsIgnoreCase("style")) {
                if (lexer.token() == ModelToken.LITERAL_INT) {
                    style = lexer.intValue();
                    lexer.nextToken();
                } else {
                    throw new ModelException("syntax error");
                }
            } else if (key.equalsIgnoreCase("size")) {
                if (lexer.token() == ModelToken.LITERAL_INT) {
                    size = lexer.intValue();
                    lexer.nextToken();
                } else {
                    throw new ModelException("syntax error");
                }
            } else {
                throw new ModelException("syntax error, " + key);
            }

            if (lexer.token() == ModelToken.COMMA) {
                lexer.nextToken(ModelToken.LITERAL_STRING);
            }
        }

        return new Font(name, style, size);
    }
    
    protected Color parseColor(DefaultModelParser parser) {
        ModelLexer lexer = parser.lexer;
        
        int r = 0, g = 0, b = 0, alpha = 0;
        for (;;) {
            if (lexer.token() == ModelToken.RBRACE) {
                lexer.nextToken();
                break;
            }

            String key;
            if (lexer.token() == ModelToken.LITERAL_STRING) {
                key = lexer.stringVal();
                lexer.nextTokenWithColon(ModelToken.LITERAL_INT);
            } else {
                throw new ModelException("syntax error");
            }

            int val;
            if (lexer.token() == ModelToken.LITERAL_INT) {
                val = lexer.intValue();
                lexer.nextToken();
            } else {
                throw new ModelException("syntax error");
            }

            if (key.equalsIgnoreCase("r")) {
                r = val;
            } else if (key.equalsIgnoreCase("g")) {
                g = val;
            } else if (key.equalsIgnoreCase("b")) {
                b = val;
            } else if (key.equalsIgnoreCase("alpha")) {
                alpha = val;
            } else {
                throw new ModelException("syntax error, " + key);
            }

            if (lexer.token() == ModelToken.COMMA) {
                lexer.nextToken(ModelToken.LITERAL_STRING);
            }
        }

        return new Color(r, g, b, alpha);
    }
    
    protected Rectangle parseRectangle(DefaultModelParser parser) {
        ModelLexer lexer = parser.lexer;
        
        int x = 0, y = 0, width = 0, height = 0;
        for (;;) {
            if (lexer.token() == ModelToken.RBRACE) {
                lexer.nextToken();
                break;
            }

            String key;
            if (lexer.token() == ModelToken.LITERAL_STRING) {
                key = lexer.stringVal();
                lexer.nextTokenWithColon(ModelToken.LITERAL_INT);
            } else {
                throw new ModelException("syntax error");
            }

            int val;
            if (lexer.token() == ModelToken.LITERAL_INT) {
                val = lexer.intValue();
                lexer.nextToken();
            } else {
                throw new ModelException("syntax error");
            }

            if (key.equalsIgnoreCase("x")) {
                x = val;
            } else if (key.equalsIgnoreCase("y")) {
                y = val;
            } else if (key.equalsIgnoreCase("width")) {
                width = val;
            } else if (key.equalsIgnoreCase("height")) {
                height = val;
            } else {
                throw new ModelException("syntax error, " + key);
            }

            if (lexer.token() == ModelToken.COMMA) {
                lexer.nextToken(ModelToken.LITERAL_STRING);
            }
        }

        return new Rectangle(x, y, width, height);
    }

    protected Point parsePoint(DefaultModelParser parser) {
        ModelLexer lexer = parser.lexer;
        
        int x = 0, y = 0;
        for (;;) {
            if (lexer.token() == ModelToken.RBRACE) {
                lexer.nextToken();
                break;
            }

            String key;
            if (lexer.token() == ModelToken.LITERAL_STRING) {
                key = lexer.stringVal();

                if (Model.DEFAULT_TYPE_KEY.equals(key)) {
                    parser.acceptType("java.awt.Point");
                    continue;
                }

                lexer.nextTokenWithColon(ModelToken.LITERAL_INT);
            } else {
                throw new ModelException("syntax error");
            }

            int val;
            if (lexer.token() == ModelToken.LITERAL_INT) {
                val = lexer.intValue();
                lexer.nextToken();
            } else {
                throw new ModelException("syntax error : " + lexer.tokenName());
            }

            if (key.equalsIgnoreCase("x")) {
                x = val;
            } else if (key.equalsIgnoreCase("y")) {
                y = val;
            } else {
                throw new ModelException("syntax error, " + key);
            }

            if (lexer.token() == ModelToken.COMMA) {
                lexer.nextToken(ModelToken.LITERAL_STRING);
            }
        }

        return new Point(x, y);
    }

    public int getFastMatchToken() {
        return ModelToken.LBRACE;
    }
}
