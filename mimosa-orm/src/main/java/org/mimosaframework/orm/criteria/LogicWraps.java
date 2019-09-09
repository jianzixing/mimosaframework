package org.mimosaframework.orm.criteria;

import java.util.*;

/**
 * 使用链结构
 * 每一条链对应的一个括号括起来的高优先级条件
 */
public class LogicWraps<T> extends LinkedList<LogicWrapObject<T>> {

    public void addLast(LogicWraps<T> lw) {
        for (LogicWrapObject<T> lwo : lw) {
            this.addLast(lwo);
        }
    }

    public void addLast(LogicWraps<T> lw, CriteriaLogic logic) {
        if (lw != null) {
            LogicWrapObject<T> last = this.getLast();
            if (last != null) {
                last.setLogic(logic);
            }
            for (LogicWrapObject<T> lwo : lw) {
                this.addLast(lwo);
            }
        }
    }

    public void addLast(LogicWrapObject<T> lwo, CriteriaLogic logic) {
        if (lwo != null) {
            LogicWrapObject<T> last = this.getLast();
            if (last != null) {
                last.setLogic(logic);
            }
            this.addLast(lwo);
        }
    }

    public void addLastLink(LogicWraps<T> lw) {
        if (lw != null) {
            this.addLastLink(lw, CriteriaLogic.AND);
        }
    }

    public void addLastLink(LogicWraps<T> lw, CriteriaLogic logic) {
        if (lw != null) {
            LogicWrapObject<T> newLwo = new LogicWrapObject<>(lw);
            newLwo.setLink(lw);
            this.addLast(newLwo, logic);
        }
    }

    public LogicWraps() {
        super();
    }

    public LogicWraps(Collection<? extends LogicWrapObject<T>> c) {
        super(c);
    }

    @Override
    public LogicWrapObject<T> getFirst() {
        try {
            return super.getFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public LogicWrapObject<T> getLast() {
        try {
            return super.getLast();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public LogicWrapObject<T> removeFirst() {
        return super.removeFirst();
    }

    @Override
    public LogicWrapObject<T> removeLast() {
        return super.removeLast();
    }

    @Override
    public void addFirst(LogicWrapObject<T> tLogicWrapObject) {
        super.addFirst(tLogicWrapObject);
    }

    @Override
    public void addLast(LogicWrapObject<T> tLogicWrapObject) {
        super.addLast(tLogicWrapObject);
    }

    @Override
    public boolean contains(Object o) {
        return super.contains(o);
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public boolean add(LogicWrapObject<T> tLogicWrapObject) {
        return super.add(tLogicWrapObject);
    }

    @Override
    public boolean remove(Object o) {
        return super.remove(o);
    }

    @Override
    public boolean addAll(Collection<? extends LogicWrapObject<T>> c) {
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends LogicWrapObject<T>> c) {
        return super.addAll(index, c);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public LogicWrapObject<T> get(int index) {
        return super.get(index);
    }

    @Override
    public LogicWrapObject<T> set(int index, LogicWrapObject<T> element) {
        return super.set(index, element);
    }

    @Override
    public void add(int index, LogicWrapObject<T> element) {
        super.add(index, element);
    }

    @Override
    public LogicWrapObject<T> remove(int index) {
        return super.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return super.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return super.lastIndexOf(o);
    }

    @Override
    public LogicWrapObject<T> peek() {
        return super.peek();
    }

    @Override
    public LogicWrapObject<T> element() {
        return super.element();
    }

    @Override
    public LogicWrapObject<T> poll() {
        return super.poll();
    }

    @Override
    public LogicWrapObject<T> remove() {
        return super.remove();
    }

    @Override
    public boolean offer(LogicWrapObject<T> tLogicWrapObject) {
        return super.offer(tLogicWrapObject);
    }

    @Override
    public boolean offerFirst(LogicWrapObject<T> tLogicWrapObject) {
        return super.offerFirst(tLogicWrapObject);
    }

    @Override
    public boolean offerLast(LogicWrapObject<T> tLogicWrapObject) {
        return super.offerLast(tLogicWrapObject);
    }

    @Override
    public LogicWrapObject<T> peekFirst() {
        return super.peekFirst();
    }

    @Override
    public LogicWrapObject<T> peekLast() {
        return super.peekLast();
    }

    @Override
    public LogicWrapObject<T> pollFirst() {
        return super.pollFirst();
    }

    @Override
    public LogicWrapObject<T> pollLast() {
        return super.pollLast();
    }

    @Override
    public void push(LogicWrapObject<T> tLogicWrapObject) {
        super.push(tLogicWrapObject);
    }

    @Override
    public LogicWrapObject<T> pop() {
        return super.pop();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return super.removeFirstOccurrence(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return super.removeLastOccurrence(o);
    }

    @Override
    public ListIterator<LogicWrapObject<T>> listIterator(int index) {
        return super.listIterator(index);
    }

    @Override
    public Iterator<LogicWrapObject<T>> descendingIterator() {
        return super.descendingIterator();
    }

    @Override
    public Object clone() {
        return super.clone();
    }

    @Override
    public Object[] toArray() {
        return super.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return super.toArray(a);
    }

    @Override
    public Iterator<LogicWrapObject<T>> iterator() {
        return super.iterator();
    }

    @Override
    public ListIterator<LogicWrapObject<T>> listIterator() {
        return super.listIterator();
    }

    @Override
    public List<LogicWrapObject<T>> subList(int fromIndex, int toIndex) {
        return super.subList(fromIndex, toIndex);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return super.containsAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return super.retainAll(c);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
//public class LogicWraps<T> implements Iterable<LogicWraps> {
//    private T where;
//    private LogicWraps next;
//    private LogicWraps previous;
//    private LogicWraps link;
//    private CriteriaLogic logic = CriteriaLogic.AND;
//
//    public static <T> LogicWraps<T> getLastLogicWraps(LogicWraps<T> logicWraps) {
//        while (logicWraps.hasNext()) logicWraps = logicWraps.next();
//        return logicWraps;
//    }
//
//    public static void addToLast(LogicWraps old, LogicWraps lw, CriteriaLogic logic) {
//        while (lw.getPrevious() != null) {
//            lw = lw.getPrevious();
//        }
//        LogicWraps<Filter> last = getLastLogicWraps(old);
//        last.setLogic(logic);
//        last.setNext(lw);
//        lw.setPrevious(last);
//    }
//
//    public static <T> void addNewToLast(LogicWraps lw, T t, CriteriaLogic logic) {
//        LogicWraps<T> last = getLastLogicWraps(lw);
//        LogicWraps<T> ins = new LogicWraps<>();
//        ins.setWhere(t);
//        ins.setPrevious(last);
//        last.setNext(ins);
//        last.setLogic(logic);
//    }
//
//    public boolean hasNext() {
//        if (next != null) {
//            return true;
//        }
//        return false;
//    }
//
//    public LogicWraps next() {
//        return next;
//    }
//
//    public LogicWraps getLink() {
//        return link;
//    }
//
//    public CriteriaLogic getLogic() {
//        return logic;
//    }
//
//    public T getWhere() {
//        return where;
//    }
//
//    public void setWhere(T where) {
//        this.where = where;
//    }
//
//    public void setNext(LogicWraps next) {
//        this.next = next;
//    }
//
//    public void setPrevious(LogicWraps previous) {
//        this.previous = previous;
//    }
//
//    public void setLink(LogicWraps link) {
//        this.link = link;
//    }
//
//    public void setLogic(CriteriaLogic logic) {
//        this.logic = logic;
//    }
//
//    public LogicWraps getNext() {
//        return next;
//    }
//
//    public LogicWraps getPrevious() {
//        return previous;
//    }
//
//    public LogicWraps clone(CloneCallback callback) {
//        LogicWraps lw = new LogicWraps();
//        if (where != null) lw.where = where;
//        if (next != null) {
//            lw.next = next.clone(callback);
//            lw.next.previous = lw;
//        }
//        if (link != null) lw.link = link.clone(callback);
//        if (logic != null) lw.logic = logic;
//        Object addLinked = callback.call(lw);
//        if (addLinked != null) {
//            LogicWraps insert = new LogicWraps();
//            insert.where = addLinked;
//            insert.previous = lw;
//            insert.next = lw.next;
//            lw.next = insert;
//            insert.link = lw.link;
//            lw.link = null;
//            insert.logic = lw.logic;
//            lw.logic = CriteriaLogic.AND;
//            return insert;
//        }
//        return lw;
//    }
//
//    @Override
//    public Iterator<LogicWraps> iterator() {
//        Iterator iterator = (new Iterator<LogicWraps>() {
//            private LogicWraps logicWraps;
//
//            public Iterator setLogicWraps(LogicWraps logicWraps) {
//                this.logicWraps = logicWraps;
//                return this;
//            }
//
//            @Override
//            public boolean hasNext() {
//                return this.logicWraps.hasNext();
//            }
//
//            @Override
//            public LogicWraps next() {
//                LogicWraps lw = this.logicWraps.next();
//                this.logicWraps = lw;
//                return lw;
//            }
//
//            @Override
//            public void remove() {
//
//            }
//        }).setLogicWraps(this);
//        return iterator;
//    }
//
//    public interface CloneCallback {
//        Object call(LogicWraps logicWraps);
//    }
//}
