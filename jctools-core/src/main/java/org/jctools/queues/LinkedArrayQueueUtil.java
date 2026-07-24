package org.jctools.queues;

import org.jctools.util.InternalAPI;

import static org.jctools.util.UnsafeRefArrayAccess.REF_ARRAY_BASE;
import static org.jctools.util.UnsafeRefArrayAccess.REF_ELEMENT_SHIFT;

/**
 * This is used for method substitution in the LinkedArray classes code generation.
 */
@InternalAPI
public final class LinkedArrayQueueUtil
{
    public static int length(Object[] buf)
    {
        return buf.length;
    }

    /**
     * This method assumes index is actually (index << 1) because lower bit is
     * used for resize. This is compensated for by reducing the element shift.
     * The computation is constant folded, so there's no cost.
     * 相当于 calcCircularRefElementOffset 方法 除以 2
     * 因为这里的 index 均是真实 index 的 2 倍，因为 index 必须是偶数，低位置 1 变为奇数表示正在扩容
     * see : org.jctools.util.UnsafeRefArrayAccess#calcCircularRefElementOffset(long, long)
     */
    public static long modifiedCalcCircularRefElementOffset(long index, long mask)
    {
        // 这里 REF_ELEMENT_SHIFT - 1 的目的是我们要获取的是插入位置的起始地址
        // 比如 index = 2 ， mask = 14, REF_ELEMENT_SHIFT = 2(数据元素类型4字节)
        // 但是这里的插入位置不能根据 index 来，因为 index 的步长是 2， mask 以及 maxCapacity 也是真实情况的两倍
        // 但是真实的数据容量是 capacity + 1, 多出一个位置是用来存放扩容后数组的地址
        // capacity 是 2 的次幂，数组只能存放 capacity 个元素，index 这里其实是真实 index 的两倍
        // 所以这里在计算插入位置时要除以 2 才能得到真正的插入位置
        // 比如 index & mask = 2，这其实表示我们要在数组第 1 个位置插入元素，每个元素占用 4 字节
        // 第二个位置的起始地址正好就是 (index & mask) << (REF_ELEMENT_SHIFT - 1)
        // 2 << 1 = 4 正好是第一个位置的起始地址
        return REF_ARRAY_BASE + ((index & mask) << (REF_ELEMENT_SHIFT - 1)); // 这里的 -1 相当于除以 2
    }

    public static long nextArrayOffset(Object[] curr)
    {
        return REF_ARRAY_BASE + ((long) (length(curr) - 1) << REF_ELEMENT_SHIFT);
    }
}
