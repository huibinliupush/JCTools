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
package org.jctools.queues;

import static org.jctools.util.UnsafeAccess.UNSAFE;
import static org.jctools.util.UnsafeAccess.fieldOffset;
import static org.jctools.util.UnsafeRefArrayAccess.*;

abstract class MpscArrayQueueL1Pad<E> extends ConcurrentCircularArrayQueue<E>
{
    byte b000,b001,b002,b003,b004,b005,b006,b007;//  8b
    byte b010,b011,b012,b013,b014,b015,b016,b017;// 16b
    byte b020,b021,b022,b023,b024,b025,b026,b027;// 24b
    byte b030,b031,b032,b033,b034,b035,b036,b037;// 32b
    byte b040,b041,b042,b043,b044,b045,b046,b047;// 40b
    byte b050,b051,b052,b053,b054,b055,b056,b057;// 48b
    byte b060,b061,b062,b063,b064,b065,b066,b067;// 56b
    byte b070,b071,b072,b073,b074,b075,b076,b077;// 64b
    byte b100,b101,b102,b103,b104,b105,b106,b107;// 72b
    byte b110,b111,b112,b113,b114,b115,b116,b117;// 80b
    byte b120,b121,b122,b123,b124,b125,b126,b127;// 88b
    byte b130,b131,b132,b133,b134,b135,b136,b137;// 96b
    byte b140,b141,b142,b143,b144,b145,b146,b147;//104b
    byte b150,b151,b152,b153,b154,b155,b156,b157;//112b
    byte b160,b161,b162,b163,b164,b165,b166,b167;//120b
    // byte b170,b171,b172,b173,b174,b175,b176,b177;//128b

    MpscArrayQueueL1Pad(int capacity)
    {
        super(capacity);
    }
}

//$gen:ordered-fields
abstract class MpscArrayQueueProducerIndexField<E> extends MpscArrayQueueL1Pad<E>
{
    private final static long P_INDEX_OFFSET = fieldOffset(MpscArrayQueueProducerIndexField.class, "producerIndex");

    private volatile long producerIndex;

    MpscArrayQueueProducerIndexField(int capacity)
    {
        super(capacity);
    }

    @Override
    public final long lvProducerIndex()
    {
        return producerIndex;
    }

    final boolean casProducerIndex(long expect, long newValue)
    {
        return UNSAFE.compareAndSwapLong(this, P_INDEX_OFFSET, expect, newValue);
    }
}

abstract class MpscArrayQueueMidPad<E> extends MpscArrayQueueProducerIndexField<E>
{
    byte b000,b001,b002,b003,b004,b005,b006,b007;//  8b
    byte b010,b011,b012,b013,b014,b015,b016,b017;// 16b
    byte b020,b021,b022,b023,b024,b025,b026,b027;// 24b
    byte b030,b031,b032,b033,b034,b035,b036,b037;// 32b
    byte b040,b041,b042,b043,b044,b045,b046,b047;// 40b
    byte b050,b051,b052,b053,b054,b055,b056,b057;// 48b
    byte b060,b061,b062,b063,b064,b065,b066,b067;// 56b
    byte b070,b071,b072,b073,b074,b075,b076,b077;// 64b
    byte b100,b101,b102,b103,b104,b105,b106,b107;// 72b
    byte b110,b111,b112,b113,b114,b115,b116,b117;// 80b
    byte b120,b121,b122,b123,b124,b125,b126,b127;// 88b
    byte b130,b131,b132,b133,b134,b135,b136,b137;// 96b
    byte b140,b141,b142,b143,b144,b145,b146,b147;//104b
    byte b150,b151,b152,b153,b154,b155,b156,b157;//112b
    byte b160,b161,b162,b163,b164,b165,b166,b167;//120b
    byte b170,b171,b172,b173,b174,b175,b176,b177;//128b

    MpscArrayQueueMidPad(int capacity)
    {
        super(capacity);
    }
}

//$gen:ordered-fields
abstract class MpscArrayQueueProducerLimitField<E> extends MpscArrayQueueMidPad<E>
{
    private final static long P_LIMIT_OFFSET = fieldOffset(MpscArrayQueueProducerLimitField.class, "producerLimit");

    // First unavailable index the producer may claim up to before rereading the consumer index
    private volatile long producerLimit;

    MpscArrayQueueProducerLimitField(int capacity)
    {
        super(capacity);
        this.producerLimit = capacity;
    }

    final long lvProducerLimit()
    {
        return producerLimit;
    }

    final void soProducerLimit(long newValue)
    {
        UNSAFE.putOrderedLong(this, P_LIMIT_OFFSET, newValue);
    }
}

abstract class MpscArrayQueueL2Pad<E> extends MpscArrayQueueProducerLimitField<E>
{
    byte b000,b001,b002,b003,b004,b005,b006,b007;//  8b
    byte b010,b011,b012,b013,b014,b015,b016,b017;// 16b
    byte b020,b021,b022,b023,b024,b025,b026,b027;// 24b
    byte b030,b031,b032,b033,b034,b035,b036,b037;// 32b
    byte b040,b041,b042,b043,b044,b045,b046,b047;// 40b
    byte b050,b051,b052,b053,b054,b055,b056,b057;// 48b
    byte b060,b061,b062,b063,b064,b065,b066,b067;// 56b
    byte b070,b071,b072,b073,b074,b075,b076,b077;// 64b
    byte b100,b101,b102,b103,b104,b105,b106,b107;// 72b
    byte b110,b111,b112,b113,b114,b115,b116,b117;// 80b
    byte b120,b121,b122,b123,b124,b125,b126,b127;// 88b
    byte b130,b131,b132,b133,b134,b135,b136,b137;// 96b
    byte b140,b141,b142,b143,b144,b145,b146,b147;//104b
    byte b150,b151,b152,b153,b154,b155,b156,b157;//112b
    byte b160,b161,b162,b163,b164,b165,b166,b167;//120b
    // byte b170,b171,b172,b173,b174,b175,b176,b177;//128b

    MpscArrayQueueL2Pad(int capacity)
    {
        super(capacity);
    }
}

//$gen:ordered-fields
abstract class MpscArrayQueueConsumerIndexField<E> extends MpscArrayQueueL2Pad<E>
{
    private final static long C_INDEX_OFFSET = fieldOffset(MpscArrayQueueConsumerIndexField.class, "consumerIndex");

    private volatile long consumerIndex;

    MpscArrayQueueConsumerIndexField(int capacity)
    {
        super(capacity);
    }

    @Override
    public final long lvConsumerIndex()
    {
        return consumerIndex;
    }

    final long lpConsumerIndex()
    {
        return UNSAFE.getLong(this, C_INDEX_OFFSET);
    }

    final void soConsumerIndex(long newValue)
    {
        UNSAFE.putOrderedLong(this, C_INDEX_OFFSET, newValue);
    }
}

abstract class MpscArrayQueueL3Pad<E> extends MpscArrayQueueConsumerIndexField<E>
{
    byte b000,b001,b002,b003,b004,b005,b006,b007;//  8b
    byte b010,b011,b012,b013,b014,b015,b016,b017;// 16b
    byte b020,b021,b022,b023,b024,b025,b026,b027;// 24b
    byte b030,b031,b032,b033,b034,b035,b036,b037;// 32b
    byte b040,b041,b042,b043,b044,b045,b046,b047;// 40b
    byte b050,b051,b052,b053,b054,b055,b056,b057;// 48b
    byte b060,b061,b062,b063,b064,b065,b066,b067;// 56b
    byte b070,b071,b072,b073,b074,b075,b076,b077;// 64b
    byte b100,b101,b102,b103,b104,b105,b106,b107;// 72b
    byte b110,b111,b112,b113,b114,b115,b116,b117;// 80b
    byte b120,b121,b122,b123,b124,b125,b126,b127;// 88b
    byte b130,b131,b132,b133,b134,b135,b136,b137;// 96b
    byte b140,b141,b142,b143,b144,b145,b146,b147;//104b
    byte b150,b151,b152,b153,b154,b155,b156,b157;//112b
    byte b160,b161,b162,b163,b164,b165,b166,b167;//120b
    byte b170,b171,b172,b173,b174,b175,b176,b177;//128b

    MpscArrayQueueL3Pad(int capacity)
    {
        super(capacity);
    }
}

/**
 * A Multi-Producer-Single-Consumer queue based on a {@link org.jctools.queues.ConcurrentCircularArrayQueue}. This
 * implies that any thread may call the offer method, but only a single thread may call poll/peek for correctness to
 * maintained. <br>
 * This implementation follows patterns documented on the package level for False Sharing protection.<br>
 * This implementation is using the <a href="http://sourceforge.net/projects/mc-fastflow/">Fast Flow</a>
 * method for polling from the queue (with minor change to correctly publish the index) and an extension of
 * the Leslie Lamport concurrent queue algorithm (originated by Martin Thompson) on the producer side.
 */
public class MpscArrayQueue<E> extends MpscArrayQueueL3Pad<E>
{
    // io.netty.buffer.PoolThreadCache.MemoryRegionCache#MemoryRegionCache
    // netty 内存池缓存
    public MpscArrayQueue(final int capacity)
    {
        super(capacity);
    }

    /**
     * {@link #offer}} if {@link #size()} is less than threshold.
     *
     * @param e         the object to offer onto the queue, not null
     * @param threshold the maximum allowable size
     * @return true if the offer is successful, false if queue size exceeds threshold
     * @since 1.0.1
     */
    public boolean offerIfBelowThreshold(final E e, int threshold)
    {
        if (null == e)
        {
            throw new NullPointerException();
        }

        final long mask = this.mask;
        final long capacity = mask + 1;

        long producerLimit = lvProducerLimit();
        long pIndex;
        do
        {
            pIndex = lvProducerIndex();
            long available = producerLimit - pIndex;
            long size = capacity - available;
            if (size >= threshold)
            {
                final long cIndex = lvConsumerIndex();
                size = pIndex - cIndex;
                if (size >= threshold)
                {
                    return false; // the size exceeds threshold
                }
                else
                {
                    // update producer limit to the next index that we must recheck the consumer index
                    producerLimit = cIndex + capacity;

                    // this is racy, but the race is benign
                    soProducerLimit(producerLimit);
                }
            }
        }
        while (!casProducerIndex(pIndex, pIndex + 1));
        /*
         * NOTE: the new producer index value is made visible BEFORE the element in the array. If we relied on
         * the index visibility to poll() we would need to handle the case where the element is not visible.
         */

        // Won CAS, move on to storing
        final long offset = calcCircularRefElementOffset(pIndex, mask);
        soRefElement(buffer, offset, e);
        return true; // AWESOME :)
    }

    /**
     * {@inheritDoc} <br>
     * <p>
     * IMPLEMENTATION NOTES:<br>
     * Lock free offer using a single CAS. As class name suggests access is permitted to many threads
     * concurrently.
     *
     * @see java.util.Queue#offer
     * @see org.jctools.queues.MessagePassingQueue#offer
     *
     * 关键是确定元素的添加位置 ProducerIndex，从而确定元素在环形数组 buffer 中的 offset
     */
    @Override
    public boolean offer(final E e)
    {
        if (null == e)
        {
            throw new NullPointerException();
        }

        // use a cached view on consumer index (potentially updated in loop)
        // actualCapacity - 1
        final long mask = this.mask;
        long producerLimit = lvProducerLimit();
        long pIndex;
        do
        {
            // 获取生产生 index
            pIndex = lvProducerIndex();
            // ProducerIndex 不能超过容量限制 producerLimit
            // 只有在 ProducerIndex < producerLimit 的时候生产者才可以向队列中添加元素
            if (pIndex >= producerLimit)
            {
                // 如果队列已满，就需要通过 ConsumerIndex 重新更新 producerLimit
                // 因为已经被消费过的元素位置，可以重新被生产者放入
                // 对于环形数组来说，这里的 ProducerIndex，ConsumerIndex，producerLimit 全部是绝对值，只增不减，同时间轮的设计

                // 旧版本知识点补充：因为生产者有多个线程，所以 MpscArrayQueue 采用了 UNSAFE.getLongVolatile() 方法保证获取消费者索引 consumerIndex 的准确性。
                // getLongVolatile() 使用了 StoreLoad Barrier，对于 Store1，StoreLoad，Load2 的操作序列，在 Load2 以及后续的读取操作之前，
                // 都会保证 Store1 的写入操作对其他处理器可见。StoreLoad 是四种内存屏障开销最大的，现在你是不是可以体会到引入 producerLimit 的好处了呢？
                // 假设我们的消费速度和生产速度比较均衡的情况下，差不多走完一圈数组才需要获取一次消费者索引 consumerIndex，
                // 从而大幅度减少了 getLongVolatile() 操作的执行次数，性能提升是显著的。
                final long cIndex = lvConsumerIndex();
                // 更新 producerLimit ，在原有容量 actualCapacity 的基础之上加上 ConsumerIndex（已经被消费的位置可以重复利用）
                producerLimit = cIndex + mask + 1;
                // 这次队列就是真的满了，生产速度大于消费速度
                if (pIndex >= producerLimit)
                {
                    return false; // FULL :(
                }
                else
                {
                    // update producer limit to the next index that we must recheck the consumer index
                    // this is racy, but the race is benign
                    // 更新 producerLimit
                    // putOrderedLong 使用的是 LazySet 延迟更新机制,
                    // putOrderedLong 只确保写入的顺序性，但不保证其他线程能立即看到最新的值。
                    // 它确保在写入操作之前的所有写操作（包括非 volatile 写）对其他线程可见，但不会像 volatile 写那样强制刷新所有缓存。
                    // 它不会引入完全的内存屏障（full memory fence），因此性能比 volatile 写更高。
                    // 适用于需要高性能的场景，同时需要确保某些字段的写入顺序性。
                    // 通常用于实现无锁数据结构（如队列、栈等），在这些场景中，写入的顺序性比全局可见性更重要。

                    // putOrderedLong使用的是StoreStore屏障。这确保了在该屏障之前的所有存储操作（写操作）在该屏障之后的存储操作之前完成，并且对其他线程可见
                    // 不会强制立即刷新到主内存，因此其他线程可能不会立即看到最新的值，但在某些情况下，比如后续有volatile写或者同步块时，这些值会被正确同步
                    // StoreStore 屏障确保：在屏障之前的所有普通写操作（非 volatile 写）对其他线程可见后，屏障之后的写操作才会执行。
                    // 但它不会强制刷新所有缓存，也不保证其他线程能立即看到最新的值

                    // volatile写使用 StoreLoad 屏障(开销最大,因 StoreLoad 屏障的全局缓存刷新)，保证可见性和顺序性，但性能较低；而putOrderedLong使用StoreStore，平衡了性能和顺序性。

                    // 可能用户还有一个误区，认为putOrderedLong能完全替代volatile，但实际上它需要配合其他同步机制，比如后续的volatile变量访问，
                    // 才能确保可见性。这时候需要强调正确使用这些方法的重要性，避免出现内存可见性问题导致的bug。

                    // putOrderedLong 的写入结果不会立即刷新到主内存，但后续如果有 volatile 写（随便一个 volatile 变量）或 synchronized 块，会触发缓存刷新。
                    // 其他线程可能不会立即看到写入的值，但能保证看到写入的顺序性。
                    soProducerLimit(producerLimit);
                }
            }
        }
        while (!casProducerIndex(pIndex, pIndex + 1)); // 多线程并发添加，CAS 更新 ProducerIndex
        // CAS 触发 ProducerIndex 的 volatile 写，触发 StoreStore，StoreLoad 屏障，ProducerIndex 之前的所有写操作均对后续的读写操作可见
        // 保证了 ProducerLimit 写的可见性

        // 生产者根据 ProducerIndex 在 环形 buffer 数组中的位置来添加元素，并发添加的时候逻辑是先确定添加位置 ProducerIndex
        // 其他线程 CAS 失败，会在 do while 中重新获取添加位置 ProducerIndex

        /*
         * NOTE: the new producer index value is made visible BEFORE the element in the array. If we relied on
         * the index visibility to poll() we would need to handle the case where the element is not visible.
         */

        // Won CAS, move on to storing
        // 计算 pIndex 在环形数组 buffer 中的 offset 偏移
        final long offset = calcCircularRefElementOffset(pIndex, mask);
        // 向队列中添加元素
        // putOrderedObject() 使用的是 LazySet 延迟更新机制，所以性能方面 putOrderedObject() 要比 putObject() 高很多
        // 这种方法通常仅在底层字段是 Java 的 volatile 字段时有用（或者如果是数组元素，该元素仅通过 volatile 访问方式访问时有用）。
        // putOrderedObject() 并不会立刻将数据更新到内存中，并把其他 Cache Line 置为失效
        // putOrderedObject() 使用了 StoreStore Barrier，对于 Store1，StoreStore，Store2 这样的操作序列，在 Store2 进行写入之前，会保证 Store1 的写操作对其他处理器可见
        // LazySet 机制是有代价的，就是写操作结果有纳秒级的延迟，不会立刻被其他线程以及自身线程可见。
        // 因为在 Mpsc Queue 的使用场景中，多个生产者只负责写入数据，并没有写入之后立刻读取的需求，
        // 所以使用 LazySet 机制是没有问题的，只要 StoreStore Barrier 保证多线程写入的顺序即可。
        soRefElement(buffer, offset, e);
        return true; // AWESOME :)
    }

    /**
     * A wait free alternative to offer which fails on CAS failure.
     *
     * @param e new element, not null
     * @return 1 if next element cannot be filled, -1 if CAS failed, 0 if successful
     */
    public final int failFastOffer(final E e)
    {
        if (null == e)
        {
            throw new NullPointerException();
        }
        final long mask = this.mask;
        final long capacity = mask + 1;
        final long pIndex = lvProducerIndex();
        long producerLimit = lvProducerLimit();
        if (pIndex >= producerLimit)
        {
            final long cIndex = lvConsumerIndex();
            producerLimit = cIndex + capacity;
            if (pIndex >= producerLimit)
            {
                return 1; // FULL :(
            }
            else
            {
                // update producer limit to the next index that we must recheck the consumer index
                soProducerLimit(producerLimit);
            }
        }

        // look Ma, no loop!
        if (!casProducerIndex(pIndex, pIndex + 1))
        {
            return -1; // CAS FAIL :(
        }

        // Won CAS, move on to storing
        final long offset = calcCircularRefElementOffset(pIndex, mask);
        soRefElement(buffer, offset, e);
        return 0; // AWESOME :)
    }

    /**
     * {@inheritDoc}
     * <p>
     * IMPLEMENTATION NOTES:<br>
     * Lock free poll using ordered loads/stores. As class name suggests access is limited to a single thread.
     *
     * 单线程执行
     * @see java.util.Queue#poll
     * @see org.jctools.queues.MessagePassingQueue#poll
     */
    @Override
    public E poll()
    {
        // 无内存屏障
        final long cIndex = lpConsumerIndex();
        // 获取 ConsumerIndex 在环形数组 buffer 中的偏移
        final long offset = calcCircularRefElementOffset(cIndex, mask);
        // Copy field to avoid re-reading after volatile load
        final E[] buffer = this.buffer;

        // If we can't see the next available element we can't poll
        // Volatile 读取数组中的元素：see UNSAFE.putOrderedObject 方法
        E e = lvRefElement(buffer, offset);
        if (null == e)
        {
            /*
             * NOTE: Queue may not actually be empty in the case of a producer (P1) being interrupted after
             * winning the CAS on offer but before storing the element in the queue. Other producers may go on
             * to fill up the queue after this element.
             */
            if (cIndex != lvProducerIndex())
            {
                do
                {
                    // 生产者填充的元素还没有对消费者可见,因为生产者是通过 putOrderedObject 填充
                    e = lvRefElement(buffer, offset);
                }
                while (e == null);
            }
            else
            {
                // ConsumerIndex = ProducerIndex 表示队列是空的，无法消费元素
                return null;
            }
        }
        // 将消费位置设置为 null
        spRefElement(buffer, offset, null);
        // LazySet 更新 ConsumerIndex（StoreStore）
        // 其他线程在 offer 的时候看不到最新的也无所谓，比如新值是 4，看到的是 2 ，那么 producerLimit + 2 也是可以的
        soConsumerIndex(cIndex + 1);
        return e;
    }

    /**
     * {@inheritDoc}
     * <p>
     * IMPLEMENTATION NOTES:<br>
     * Lock free peek using ordered loads. As class name suggests access is limited to a single thread.
     *
     * @see java.util.Queue#poll
     * @see org.jctools.queues.MessagePassingQueue#poll
     */
    @Override
    public E peek()
    {
        // Copy field to avoid re-reading after volatile load
        final E[] buffer = this.buffer;

        final long cIndex = lpConsumerIndex();
        final long offset = calcCircularRefElementOffset(cIndex, mask);
        E e = lvRefElement(buffer, offset);
        if (null == e)
        {
            /*
             * NOTE: Queue may not actually be empty in the case of a producer (P1) being interrupted after
             * winning the CAS on offer but before storing the element in the queue. Other producers may go on
             * to fill up the queue after this element.
             */
            if (cIndex != lvProducerIndex())
            {
                do
                {
                    e = lvRefElement(buffer, offset);
                }
                while (e == null);
            }
            else
            {
                return null;
            }
        }
        return e;
    }

    @Override
    public boolean relaxedOffer(E e)
    {
        return offer(e);
    }

    @Override
    public E relaxedPoll()
    {
        final E[] buffer = this.buffer;
        final long cIndex = lpConsumerIndex();
        final long offset = calcCircularRefElementOffset(cIndex, mask);

        // If we can't see the next available element we can't poll
        E e = lvRefElement(buffer, offset);
        if (null == e)
        {
            return null;
        }

        spRefElement(buffer, offset, null);
        soConsumerIndex(cIndex + 1);
        return e;
    }

    @Override
    public E relaxedPeek()
    {
        final E[] buffer = this.buffer;
        final long mask = this.mask;
        final long cIndex = lpConsumerIndex();
        return lvRefElement(buffer, calcCircularRefElementOffset(cIndex, mask));
    }

    @Override
    public int drain(final Consumer<E> c, final int limit)
    {
        if (null == c)
            throw new IllegalArgumentException("c is null");
        if (limit < 0)
            throw new IllegalArgumentException("limit is negative: " + limit);
        if (limit == 0)
            return 0;

        final E[] buffer = this.buffer;
        final long mask = this.mask;
        final long cIndex = lpConsumerIndex();

        for (int i = 0; i < limit; i++)
        {
            final long index = cIndex + i;
            final long offset = calcCircularRefElementOffset(index, mask);
            final E e = lvRefElement(buffer, offset);
            if (null == e)
            {
                return i;
            }
            spRefElement(buffer, offset, null);
            soConsumerIndex(index + 1); // ordered store -> atomic and ordered for size()
            c.accept(e);
        }
        return limit;
    }

    @Override
    public int fill(Supplier<E> s, int limit)
    {
        if (null == s)
            throw new IllegalArgumentException("supplier is null");
        if (limit < 0)
            throw new IllegalArgumentException("limit is negative:" + limit);
        if (limit == 0)
            return 0;

        final long mask = this.mask;
        final long capacity = mask + 1;
        long producerLimit = lvProducerLimit();
        long pIndex;
        int actualLimit;
        do
        {
            pIndex = lvProducerIndex();
            long available = producerLimit - pIndex;
            if (available <= 0)
            {
                final long cIndex = lvConsumerIndex();
                producerLimit = cIndex + capacity;
                available = producerLimit - pIndex;
                if (available <= 0)
                {
                    return 0; // FULL :(
                }
                else
                {
                    // update producer limit to the next index that we must recheck the consumer index
                    soProducerLimit(producerLimit);
                }
            }
            actualLimit = Math.min((int) available, limit);
        }
        while (!casProducerIndex(pIndex, pIndex + actualLimit));
        // right, now we claimed a few slots and can fill them with goodness
        final E[] buffer = this.buffer;
        for (int i = 0; i < actualLimit; i++)
        {
            // Won CAS, move on to storing
            final long offset = calcCircularRefElementOffset(pIndex + i, mask);
            soRefElement(buffer, offset, s.get());
        }
        return actualLimit;
    }

    @Override
    public int drain(Consumer<E> c)
    {
        return drain(c, capacity());
    }

    @Override
    public int fill(Supplier<E> s)
    {
        return MessagePassingQueueUtil.fillBounded(this, s);
    }

    @Override
    public void drain(Consumer<E> c, WaitStrategy w, ExitCondition exit)
    {
        MessagePassingQueueUtil.drain(this, c, w, exit);
    }

    @Override
    public void fill(Supplier<E> s, WaitStrategy wait, ExitCondition exit)
    {
        MessagePassingQueueUtil.fill(this, s, wait, exit);
    }
}
