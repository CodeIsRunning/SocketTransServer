package sample;

import java.util.LinkedList;

/**
 * @author liuxf
 * @version 1.0
 * @date 2020/12/9 16:07
 */
class LimitedQueue<E> extends LinkedList<E> {
    private static final long serialVersionUID = 1L;
    private int limit;

    public LimitedQueue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E o) {
        super.add(o);
        while (size() > limit) { super.remove(); }
        return true;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        for (E e: this){
           stringBuilder.append(System.lineSeparator()).append(e).append("---接收完成").append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }
}
