/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jctools.util;

import static org.jctools.util.UnsafeAccess.UNSAFE;

@InternalAPI
public final class UnsafeRefArrayAccess
{
    // 数组实例对象数据区的起始地址（跳过数组对象头）
    public static final long REF_ARRAY_BASE;
    // 数据元素占用字节数，这里指的是引用类型的数据元素
    public static final int REF_ELEMENT_SHIFT;

    static
    {
        // 引用类型指针占用字节数，不开启压缩指针：32位系统占用4字节，64位占用8字节。开启压缩指针均占用4字节
        final int scale = UnsafeAccess.UNSAFE.arrayIndexScale(Object[].class);
        if (4 == scale)
        {
            REF_ELEMENT_SHIFT = 2;
        }
        else if (8 == scale)
        {
            REF_ELEMENT_SHIFT = 3;
        }
        else
        {
            throw new IllegalStateException("Unknown pointer size: " + scale);
        }
        REF_ARRAY_BASE = UnsafeAccess.UNSAFE.arrayBaseOffset(Object[].class);
    }

    /**
     * A plain store (no ordering/fences) of an element to a given offset
     *
     * @param buffer this.buffer
     * @param offset computed via {@link UnsafeRefArrayAccess#calcRefElementOffset(long)}
     * @param e      an orderly kitty
     */
    public static <E> void spRefElement(E[] buffer, long offset, E e)
    {
        UNSAFE.putObject(buffer, offset, e);
    }

    /**
     * An ordered store of an element to a given offset
     *
     * `putObjectVolatile(Object, long, Object)` 方法的一个变体，它不能保证其他线程能够立即访问存储的对象。
     * 通常，只有当底层字段是 Java 的 volatile 类型（或者如果是数组cell，则该cell只能通过 volatile 访问）时，此方法才有用。
     *
     * @param buffer this.buffer
     * @param offset computed via {@link UnsafeRefArrayAccess#calcCircularRefElementOffset}
     * @param e      an orderly kitty
     */
    public static <E> void soRefElement(E[] buffer, long offset, E e)
    {
        // putOrderedObject 的写操作并不能保证其他线程可见（插入的只是 store-store 屏障，保证屏障之前的写操作可见，但是本次写操作不可见）
        // 这个方法只能用于写入 volatile 变量
        // 或者写入数组，但是访问数组必须采用 volatile 方式访问
        UNSAFE.putOrderedObject(buffer, offset, e);
    }

    /**
     * A plain load (no ordering/fences) of an element from a given offset.
     *
     * @param buffer this.buffer
     * @param offset computed via {@link UnsafeRefArrayAccess#calcRefElementOffset(long)}
     * @return the element at the offset
     */
    @SuppressWarnings("unchecked")
    public static <E> E lpRefElement(E[] buffer, long offset)
    {
        return (E) UNSAFE.getObject(buffer, offset);
    }

    /**
     * A volatile load of an element from a given offset.
     * 从给定的 Java 变量中获取引用值，并带有 volatile 加载语义。除此之外，它与 getObject(Object, long) 完全相同。
     * @param buffer this.buffer
     * @param offset computed via {@link UnsafeRefArrayAccess#calcRefElementOffset(long)}
     * @return the element at the offset
     */
    @SuppressWarnings("unchecked")
    public static <E> E lvRefElement(E[] buffer, long offset)
    {
        return (E) UNSAFE.getObjectVolatile(buffer, offset);
    }

    /**
     * @param index desirable element index
     * @return the offset in bytes within the array for a given index
     */
    public static long calcRefElementOffset(long index)
    {
        return REF_ARRAY_BASE + (index << REF_ELEMENT_SHIFT);
    }

    /**
     * Note: circular arrays are assumed a power of 2 in length and the `mask` is (length - 1).
     *
     * @param index desirable element index
     * @param mask (length - 1)
     * @return the offset in bytes within the circular array for a given index
     */
    public static long calcCircularRefElementOffset(long index, long mask)
    {
        // REF_ARRAY_BASE 表示数组对象数据区的起始地址（跳过数组对象头）
        // index & mask 定位 index 在环形数组中的位置（index 是绝对值，只增不减）
        // REF_ELEMENT_SHIFT 队列元素大小
        return REF_ARRAY_BASE + ((index & mask) << REF_ELEMENT_SHIFT);
    }

    /**
     * This makes for an easier time generating the atomic queues, and removes some warnings.
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] allocateRefArray(int capacity)
    {
        return (E[]) new Object[capacity];
    }
}
