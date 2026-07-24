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

import org.jctools.queues.IndexedQueueSizeUtil.IndexedQueue;
import org.jctools.util.PortableJvmInfo;
import org.jctools.util.Pow2;
import org.jctools.util.RangeUtil;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.jctools.queues.LinkedArrayQueueUtil.length;
import static org.jctools.queues.LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset;
import static org.jctools.util.UnsafeAccess.UNSAFE;
import static org.jctools.util.UnsafeAccess.fieldOffset;
import static org.jctools.util.UnsafeRefArrayAccess.*;


abstract class BaseMpscLinkedArrayQueuePad1<E> extends AbstractQueue<E> implements IndexedQueue
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
}

// $gen:ordered-fields
abstract class BaseMpscLinkedArrayQueueProducerFields<E> extends BaseMpscLinkedArrayQueuePad1<E>
{
    private final static long P_INDEX_OFFSET = fieldOffset(BaseMpscLinkedArrayQueueProducerFields.class, "producerIndex");
    // 前面填充 128 字节
    private volatile long producerIndex;

    @Override
    public final long lvProducerIndex()
    {
        return producerIndex;
    }

    final void soProducerIndex(long newValue)
    {
        UNSAFE.putOrderedLong(this, P_INDEX_OFFSET, newValue);
    }

    final boolean casProducerIndex(long expect, long newValue)
    {
        return UNSAFE.compareAndSwapLong(this, P_INDEX_OFFSET, expect, newValue);
    }
}

abstract class BaseMpscLinkedArrayQueuePad2<E> extends BaseMpscLinkedArrayQueueProducerFields<E>
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
}

// $gen:ordered-fields
abstract class BaseMpscLinkedArrayQueueConsumerFields<E> extends BaseMpscLinkedArrayQueuePad2<E>
{
    private final static long C_INDEX_OFFSET = fieldOffset(BaseMpscLinkedArrayQueueConsumerFields.class,"consumerIndex");
    // 前面填充 128 字节
    private volatile long consumerIndex;
    protected long consumerMask;
    protected E[] consumerBuffer;

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

abstract class BaseMpscLinkedArrayQueuePad3<E> extends BaseMpscLinkedArrayQueueConsumerFields<E>
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
}

// $gen:ordered-fields
abstract class BaseMpscLinkedArrayQueueColdProducerFields<E> extends BaseMpscLinkedArrayQueuePad3<E>
{
    private final static long P_LIMIT_OFFSET = fieldOffset(BaseMpscLinkedArrayQueueColdProducerFields.class,"producerLimit");
    // 前面填充 128 字节
    // producerLimit 指向的位置是 producerBuffer 倒数第二个位置用于存放 JUMP 标识
    private volatile long producerLimit;
    // 用于针对扩容之后的新 buffer
    protected long producerMask;
    // 用于扩容之后，指向扩容之后的新 buffer, procuder 直接向新 buffer 添加元素
    // consumerBuffer 还是指向旧的 buffer,继续消费没有被消费的元素
    // 读写分离
    protected E[] producerBuffer;

    final long lvProducerLimit()
    {
        return producerLimit;
    }

    final boolean casProducerLimit(long expect, long newValue)
    {
        return UNSAFE.compareAndSwapLong(this, P_LIMIT_OFFSET, expect, newValue);
    }

    final void soProducerLimit(long newValue)
    {
        UNSAFE.putOrderedLong(this, P_LIMIT_OFFSET, newValue);
    }
}


/**
 * An MPSC array queue which starts at <i>initialCapacity</i> and grows to <i>maxCapacity</i> in linked chunks
 * of the initial size. The queue grows only when the current buffer is full and elements are not copied on
 * resize, instead a link to the new buffer is stored in the old buffer for the consumer to follow.
 */
abstract class BaseMpscLinkedArrayQueue<E> extends BaseMpscLinkedArrayQueueColdProducerFields<E>
    implements MessagePassingQueue<E>, QueueProgressIndicators
{
    // No post padding here, subclasses must add
    private static final Object JUMP = new Object();
    private static final Object BUFFER_CONSUMED = new Object();
    private static final int CONTINUE_TO_P_INDEX_CAS = 0;
    private static final int RETRY = 1;
    private static final int QUEUE_FULL = 2;
    private static final int QUEUE_RESIZE = 3;


    /**
     * @param initialCapacity the queue initial capacity. If chunk size is fixed this will be the chunk size.
     *                        Must be 2 or more.
     */
    public BaseMpscLinkedArrayQueue(final int initialCapacity)
    {
        RangeUtil.checkGreaterThanOrEqual(initialCapacity, 2, "initialCapacity");
        // 比如这里是 8
        int p2capacity = Pow2.roundToPowerOfTwo(initialCapacity);
        // leave lower bit of mask clear
        // (8 - 1) << 1 = 14, mask 最低位清 0
        // 相关 producerIndex 和 consumerIndex 增加的步长是 2，所以这里的 mask 也应该是 2 倍
        // 这样根据 index & mask 才能正确定位到 buffer 的 offset（始终为偶数）
        // producerIndex 的最低位表示是否正在扩容，所以正常情况下是偶数，只有扩容时候才是奇数
        // mask 指向的位置正好是 buffer 中存储 JUMP 的位置，因为 mask + 2 的位置存储扩容之后的新数组（记住步长为 2）
        long mask = (p2capacity - 1) << 1;
        // need extra element to point at next array
        // 9 ，多出一个位置放 next array 的指针（扩容时候用），第 8 个元素存储 JUMP 正好是 mask 指向的位置
        // 具体定位的时候要将相关 index >> 1 ，才能获得真正的位置，相关索引都是真实的两倍
        E[] buffer = allocateRefArray(p2capacity + 1);
        producerBuffer = buffer;
        producerMask = mask;
        consumerBuffer = buffer;
        consumerMask = mask;
        // producerIndex 的步长是 2 ，那么 ProducerLimit 也应该是 2 倍
        // 这里要注意的是 ProducerLimit 指向的是数组倒数第 2 个位置，数组真实容量为 p2capacity + 1
        // 比如数组容量是 9 ， ProducerLimit 真实指向的是第 8 个元素 。真实 index = 7
        // 真实 producerIndex 等于 7（第 8 个元素） 的时候就应该扩容了，位置 7 存放一个标识 JUMP ,表示这个位置的元素在下一个新数组中存放
        // 位置 8 存放下一个新的数组指针

        // 对于 MpscArrayQueue 来说, 它的 ProducerLimit 为 capacity
        soProducerLimit(mask); // we know it's all empty to start with
    }

    @Override
    public int size()
    {
        return IndexedQueueSizeUtil.size(this, IndexedQueueSizeUtil.IGNORE_PARITY_DIVISOR);
    }

    @Override
    public boolean isEmpty()
    {
        // Order matters!
        // Loading consumer before producer allows for producer increments after consumer index is read.
        // This ensures this method is conservative in it's estimate. Note that as this is an MPMC there is
        // nothing we can do to make this an exact method.
        return ((lvConsumerIndex() - lvProducerIndex()) / 2 == 0);
    }

    @Override
    public String toString()
    {
        return this.getClass().getName();
    }

    @Override
    public boolean offer(final E e)
    {
        if (null == e)
        {
            throw new NullPointerException();
        }

        long mask; // 14
        E[] buffer; // 8 + 1 = 9
        long pIndex;

        while (true)
        {
            // 初始为 mask = 14
            long producerLimit = lvProducerLimit();
            pIndex = lvProducerIndex();
            // lower bit is indicative of resize, if we see it we spin until it's cleared
            // 最低位 bit 表示扩容。0 表示扩容完毕，1 表示正在扩容
            if ((pIndex & 1) == 1)
            {
                // 如果正在扩容则自旋等待
                continue;
            }
            // 重点：pIndex is even (lower bit is 0) -> actual index is (pIndex >> 1)

            // mask/buffer may get changed by resizing -> only use for array access after successful CAS.
            // 因为 cas 是 store-load 屏障，这样在后面执行 casProducerIndex，由于插入了 store-load 屏障，所以之前的写操作全部可见
            // producerMask,producerBuffer 也就可见了
            mask = this.producerMask;
            buffer = this.producerBuffer;
            // a successful CAS ties the ordering, lv(pIndex) - [mask/buffer] -> cas(pIndex)

            // assumption behind this optimization is that queue is almost always empty or near empty
            if (producerLimit <= pIndex) // 队列满了
            {
                // 1. 首先尝试将消费过的元素位置腾挪出来，后续可以将新元素添加到这些已经被消费过的位置
                // 2. 无法腾挪出位置（生产者速度大于消费者），则对 MpscUnboundedArrayQueue 进行扩容
                int result = offerSlowPath(mask, pIndex, producerLimit);
                switch (result)
                {
                    case CONTINUE_TO_P_INDEX_CAS:
                        break;
                    case RETRY:
                        continue;
                    case QUEUE_FULL:
                        // MpscChunkedArrayQueue 会受到 maxCapacity 的限制
                        return false;
                    case QUEUE_RESIZE:
                        resize(mask, buffer, pIndex, e, null);
                        return true;
                }
            }
            // 更新 index 的步长是 2 ，而不是之前 MpscArrayQueue 中的 1
            if (casProducerIndex(pIndex, pIndex + 2)) // 凡是 cas 的地方都是用于检查是否出现多线程并发的情况
            {
                // cas 更新成功，那么本次就可以放心使用 pIndex
                // 更新失败，那么就重新获取 pIndex（表示此时多个线程并发向 mpsc 执行 offser 操作）
                // 只有一个线程能够 cas 成功，获取正确的  pIndex， 剩下的线程重新获取 pIndex 重新 cas
                break;
            }
        }
        // INDEX visible before ELEMENT
        // pIndex 是偶数，自然 mask 也应该是偶数，是之前 MpscArrayQueue 的 2 倍
        // 注意这里的 pIndex 是 casProducerIndex 之前的旧值，之所以要先 cas 是因为这里是多线程 offer
        // 先 cas 是要保证其他线程获得正确的 ProducerIndex， 旧的 ProducerIndex 在这里正在被使用
        // offset 为我们要插入位置的起始地址
        final long offset = modifiedCalcCircularRefElementOffset(pIndex, mask);
        soRefElement(buffer, offset, e); // release element e
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation is correct for single consumer thread use only.
     */
    @SuppressWarnings("unchecked")
    @Override
    public E poll()
    {
        final E[] buffer = consumerBuffer;
        final long cIndex = lpConsumerIndex();
        final long mask = consumerMask;
        // 获取真实的位置 cIndex >> 1
        final long offset = modifiedCalcCircularRefElementOffset(cIndex, mask);
        Object e = lvRefElement(buffer, offset);
        if (e == null)
        {
            long pIndex = lvProducerIndex();
            // isEmpty?
            if ((cIndex - pIndex) / 2 == 0)
            {
                return null;
            }
            // poll() == null iff queue is empty, null element is not strong enough indicator, so we must
            // spin until element is visible.
            do
            {
                /**
                 * 我大概知道这个 bug 的产生原因，这个和 jit 没关系。主要问题导致的就是 jdk 在不同平台内存屏障的实现。从而引用的可见性问题导致的这个死循环。
                 *
                 * 这个bug 产生的条件还有一些苛刻：
                 * 1. 在 poll mpsc 的时候恰巧遇到 mpsc 扩容，就会走到 while 循环里等待扩容结束
                 * 2. 扩容完之后，jctool 是用 store-store 屏障去写，一个JUMP 标识，这个用来使消费者知道扩容后的新数组位置
                 * 3. reactor 线程通过 volatile 去读，此时如果不同平台内存屏障的实现差异，可能就会导致可见性问题，导致看不到扩容之后的 JUMP 标识，一直拿到的一直是 null
                 * 关键是 store-load 这个屏障的实现，可能 jdk8 在 arm 平台实现的问题，StoreLoad 屏障的全局缓存刷新
                 *
                 * see : https://github.com/netty/netty/issues/13137
                 * */
                // putOrderedObject 写之后，并不能保证其他线程立马读到 : org.jctools.queues.BaseMpscLinkedArrayQueue.resize
                // store-store 只能保证本次写之前的写操作都能看到，但本次写不会强制刷缓存，所以其他线程可能读不到
                // 需要同步机制，或者执行一次 volatile 写触发 store-load 屏障才能可见（触发缓存刷新）
                // see : https://github.com/netty/netty/issues/13137
                // putOrderedObject 本身并不直接触发全局可见性，仍需后续操作（如 volatile 写）才能完成缓存刷新。
                // 这里应该随便写入一个 volatile 变量，触发 store-load 屏障，比如用 putOrderedObject 写了 volatile a
                // 然后这里写一下 volatile b ， 虽然变量不是同一个，但 store-load 可以保证 load(读取 a) 之前的所有写操作可见

                /**
                 * 不对，不对，这里需要再写线程中执行 volatile 写，也就是说 store-load 应该插到写线程中
                 * 这样写线程所在 cpu 缓存才能被刷入到主存中
                 *
                 * 在读线程中插入 store-load，刷入主存的是读线程 cpu 缓存，还是读取不到
                 *
                 * */

                /**
                 * 新感悟，感觉这里写的也没问题啊
                 * see : org.jctools.util.UnsafeRefArrayAccess#soRefElement(java.lang.Object[], long, java.lang.Object)
                 * putOrderedObject 的写操作并不能保证其他线程可见（插入的只是 store-store 屏障，保证屏障之前的写操作可见，但是本次写操作不可见）
                 * 这个方法只能用于写入 volatile 变量
                 * 或者写入数组，但是访问数组必须采用 volatile 方式访问
                 *
                 * 这里确实用的是 volatile 方式访问数组元素啊，应该是可见的
                 * */
                e = lvRefElement(buffer, offset);
            }
            while (e == null);
        }

        if (e == JUMP)
        {
            final E[] nextBuffer = nextBuffer(buffer, mask);
            return newBufferPoll(nextBuffer, cIndex);
        }

        soRefElement(buffer, offset, null); // release element null
        soConsumerIndex(cIndex + 2); // release cIndex
        return (E) e;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation is correct for single consumer thread use only.
     */
    @SuppressWarnings("unchecked")
    @Override
    public E peek()
    {
        final E[] buffer = consumerBuffer;
        final long cIndex = lpConsumerIndex();
        final long mask = consumerMask;

        final long offset = modifiedCalcCircularRefElementOffset(cIndex, mask);
        Object e = lvRefElement(buffer, offset);
        if (e == null)
        {
            long pIndex = lvProducerIndex();
            // isEmpty?
            if ((cIndex - pIndex) / 2 == 0)
            {
                return null;
            }
            // peek() == null iff queue is empty, null element is not strong enough indicator, so we must
            // spin until element is visible.
            do
            {
                e = lvRefElement(buffer, offset);
            }
            while (e == null);
        }
        if (e == JUMP)
        {
            return newBufferPeek(nextBuffer(buffer, mask), cIndex);
        }
        return (E) e;
    }

    /**
     * We do not inline resize into this method because we do not resize on fill.
     * 队列满了的情况下会调用到这里 ： producerLimit <= pIndex
     */
    private int offerSlowPath(long mask, long pIndex, long producerLimit)
    {
        // index 增加的步长为 2
        final long cIndex = lvConsumerIndex();
        // MpscChunkedArrayQueue : mask
        // 语义：获取 buffer 真实能够存储元素的个数，排除 JUMP , next pointer
        // 当然了这里 bufferCapacity 是语义的两倍
        long bufferCapacity = getCurrentBufferCapacity(mask);
        // 已经消费过的位置可以腾挪出来放新元素，不用扩容
        if (cIndex + bufferCapacity > pIndex) // 表示已经消费过一些了，如果没有消费过那么 cIndex + bufferCapacity = pIndex
        {
            // 更新 producerLimit 的位置，这里都是绝对位置，新的 limit 为 cIndex + bufferCapacity
            // 消费了几个位置，那么这里就会多出几个位置让我们 offer, 所以 producerLimit 要重新更新
            if (!casProducerLimit(producerLimit, cIndex + bufferCapacity))
            {
                // retry from top
                return RETRY;
            }
            else
            {
                // continue to pIndex CAS
                // 虽然现在 mpsc 已经满了，但是由于队列中存在已经被消费过的位置
                // 那么本次 offser 的位置可以占用已经被消费过的位置 cIndex 之前的
                return CONTINUE_TO_P_INDEX_CAS;
            }
        }
        // full and cannot grow，队列已经满了，但无法扩容，已经达到了最大容量
        // MpscUnboundedArrayQueue 因为是无界的，这里会返回 Integer.MAX_VALUE，不会走这个分支
        // MpscChunkedArrayQueue 的容量受到 maxCapacity 的限制，这里会返回 maxQueueCapacity - (pIndex - cIndex)
        else if (availableInQueue(pIndex, cIndex) <= 0)// 获取队列最大可用容量（包含可扩容的量）
        {
            // offer should return false;
            // MpscChunkedArrayQueue 会有队列满的情况，MpscUnboundedArrayQueue 是无界的，没有满的情况
            return QUEUE_FULL;
        }
        // grab index for resize -> set lower bit，队列虽然满了，但未达到最大容量，触发扩容
        // 当队列满的时候，MpscUnboundedArrayQueue 就会触发扩容
        else if (casProducerIndex(pIndex, pIndex + 1)) // 设置队列扩容标识， producerIndex 低位置 1
        {
            // trigger a resize
            return QUEUE_RESIZE;
        }
        else
        {
            // failed resize attempt, retry from top
            // 有可能现在多线程并发扩容，那么这里就 retry, 如果别的线程已经执行扩容了，你就不要扩了，轮询等待
            return RETRY;
        }
    }

    /**
     * @return available elements in queue * 2
     */
    protected abstract long availableInQueue(long pIndex, long cIndex);

    @SuppressWarnings("unchecked")
    private E[] nextBuffer(final E[] buffer, final long mask)
    {
        final long offset = nextArrayOffset(mask);
        final E[] nextBuffer = (E[]) lvRefElement(buffer, offset);
        consumerBuffer = nextBuffer;
        consumerMask = (length(nextBuffer) - 2) << 1;
        // 标记旧的数组已经消费完了（在就数组的最后一个位置也就是存放 newBuffer 的地方）
        soRefElement(buffer, offset, BUFFER_CONSUMED);
        // 获取扩容后的新数组
        return nextBuffer;
    }

    private static long nextArrayOffset(long mask)
    {
        // 数组最后一个位置存放新数组的指针
        // mask 指向数组倒数第 3 的位置，加 2 指向最后一个位置，用来存放下一个新数组指针
        // mask = capacity -1(语义层面，实际上要乘以2) , length = capacity + 1
        // mask + 2 = length

        // mask 指向的位置正好是 buffer 中最后一个能够存储元素的位置，因为 mask + 1 的位置要存储 JUMP 标识
        // mask + 2 的位置要存储扩容之后新数组的指针
        return modifiedCalcCircularRefElementOffset(mask + 2, Long.MAX_VALUE);
    }

    private E newBufferPoll(E[] nextBuffer, long cIndex)
    {
        final long offset = modifiedCalcCircularRefElementOffset(cIndex, consumerMask);
        final E n = lvRefElement(nextBuffer, offset);
        if (n == null)
        {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        soRefElement(nextBuffer, offset, null);
        soConsumerIndex(cIndex + 2);
        return n;
    }

    private E newBufferPeek(E[] nextBuffer, long cIndex)
    {
        final long offset = modifiedCalcCircularRefElementOffset(cIndex, consumerMask);
        final E n = lvRefElement(nextBuffer, offset);
        if (null == n)
        {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        return n;
    }

    @Override
    public long currentProducerIndex()
    {
        return lvProducerIndex() / 2;
    }

    @Override
    public long currentConsumerIndex()
    {
        return lvConsumerIndex() / 2;
    }

    @Override
    public abstract int capacity();

    @Override
    public boolean relaxedOffer(E e)
    {
        return offer(e);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E relaxedPoll()
    {
        final E[] buffer = consumerBuffer;
        final long cIndex = lpConsumerIndex();
        final long mask = consumerMask;

        final long offset = modifiedCalcCircularRefElementOffset(cIndex, mask);
        Object e = lvRefElement(buffer, offset);
        if (e == null)
        {
            return null;
        }
        if (e == JUMP)
        {
            final E[] nextBuffer = nextBuffer(buffer, mask);
            return newBufferPoll(nextBuffer, cIndex);
        }
        soRefElement(buffer, offset, null);
        soConsumerIndex(cIndex + 2);
        return (E) e;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E relaxedPeek()
    {
        final E[] buffer = consumerBuffer;
        final long cIndex = lpConsumerIndex();
        final long mask = consumerMask;

        final long offset = modifiedCalcCircularRefElementOffset(cIndex, mask);
        Object e = lvRefElement(buffer, offset);
        if (e == JUMP)
        {
            return newBufferPeek(nextBuffer(buffer, mask), cIndex);
        }
        return (E) e;
    }

    @Override
    public int fill(Supplier<E> s)
    {
        long result = 0;// result is a long because we want to have a safepoint check at regular intervals
        final int capacity = capacity();
        do
        {
            final int filled = fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH);
            if (filled == 0)
            {
                return (int) result;
            }
            result += filled;
        }
        while (result <= capacity);
        return (int) result;
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

        long mask;
        E[] buffer;
        long pIndex;
        int claimedSlots;
        while (true)
        {
            long producerLimit = lvProducerLimit();
            pIndex = lvProducerIndex();
            // lower bit is indicative of resize, if we see it we spin until it's cleared
            if ((pIndex & 1) == 1)
            {
                continue;
            }
            // pIndex is even (lower bit is 0) -> actual index is (pIndex >> 1)

            // NOTE: mask/buffer may get changed by resizing -> only use for array access after successful CAS.
            // Only by virtue offloading them between the lvProducerIndex and a successful casProducerIndex are they
            // safe to use.
            mask = this.producerMask;
            buffer = this.producerBuffer;
            // a successful CAS ties the ordering, lv(pIndex) -> [mask/buffer] -> cas(pIndex)

            // we want 'limit' slots, but will settle for whatever is visible to 'producerLimit'
            long batchIndex = Math.min(producerLimit, pIndex + 2l * limit); //  -> producerLimit >= batchIndex

            if (pIndex >= producerLimit)
            {
                int result = offerSlowPath(mask, pIndex, producerLimit);
                switch (result)
                {
                    case CONTINUE_TO_P_INDEX_CAS:
                        // offer slow path verifies only one slot ahead, we cannot rely on indication here
                    case RETRY:
                        continue;
                    case QUEUE_FULL:
                        return 0;
                    case QUEUE_RESIZE:
                        resize(mask, buffer, pIndex, null, s);
                        return 1;
                }
            }

            // claim limit slots at once
            if (casProducerIndex(pIndex, batchIndex))
            {
                claimedSlots = (int) ((batchIndex - pIndex) / 2);
                break;
            }
        }

        for (int i = 0; i < claimedSlots; i++)
        {
            final long offset = modifiedCalcCircularRefElementOffset(pIndex + 2l * i, mask);
            soRefElement(buffer, offset, s.get());
        }
        return claimedSlots;
    }

    @Override
    public void fill(Supplier<E> s, WaitStrategy wait, ExitCondition exit)
    {
        MessagePassingQueueUtil.fill(this, s, wait, exit);
    }
    @Override
    public int drain(Consumer<E> c)
    {
        return drain(c, capacity());
    }

    @Override
    public int drain(Consumer<E> c, int limit)
    {
        return MessagePassingQueueUtil.drain(this, c, limit);
    }

    @Override
    public void drain(Consumer<E> c, WaitStrategy wait, ExitCondition exit)
    {
        MessagePassingQueueUtil.drain(this, c, wait, exit);
    }

    /**
     * Get an iterator for this queue. This method is thread safe.
     * <p>
     * The iterator provides a best-effort snapshot of the elements in the queue.
     * The returned iterator is not guaranteed to return elements in queue order,
     * and races with the consumer thread may cause gaps in the sequence of returned elements.
     * Like {link #relaxedPoll}, the iterator may not immediately return newly inserted elements.
     *
     * @return The iterator.
     */
    @Override
    public Iterator<E> iterator() {
        return new WeakIterator(consumerBuffer, lvConsumerIndex(), lvProducerIndex());
    }

    private static class WeakIterator<E> implements Iterator<E>
    {
        private final long pIndex;
        private long nextIndex;
        private E nextElement;
        private E[] currentBuffer;
        private int mask;

        WeakIterator(E[] currentBuffer, long cIndex, long pIndex)
        {
            this.pIndex = pIndex >> 1;
            this.nextIndex = cIndex >> 1;
            setBuffer(currentBuffer);
            nextElement = getNext();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }

        @Override
        public boolean hasNext()
        {
            return nextElement != null;
        }

        @Override
        public E next()
        {
            final E e = nextElement;
            if (e == null)
            {
                throw new NoSuchElementException();
            }
            nextElement = getNext();
            return e;
        }

        private void setBuffer(E[] buffer)
        {
            this.currentBuffer = buffer;
            this.mask = length(buffer) - 2;
        }

        private E getNext()
        {
            while (nextIndex < pIndex)
            {
                long index = nextIndex++;
                E e = lvRefElement(currentBuffer, calcCircularRefElementOffset(index, mask));
                // skip removed/not yet visible elements
                if (e == null)
                {
                    continue;
                }

                // not null && not JUMP -> found next element
                if (e != JUMP)
                {
                    return e;
                }

                // need to jump to the next buffer
                int nextBufferIndex = mask + 1;
                Object nextBuffer = lvRefElement(currentBuffer,
                                              calcRefElementOffset(nextBufferIndex));

                if (nextBuffer == BUFFER_CONSUMED || nextBuffer == null)
                {
                    // Consumer may have passed us, or the next buffer is not visible yet: drop out early
                    return null;
                }

                setBuffer((E[]) nextBuffer);
                // now with the new array retry the load, it can't be a JUMP, but we need to repeat same index
                e = lvRefElement(currentBuffer, calcCircularRefElementOffset(index, mask));
                // skip removed/not yet visible elements
                if (e == null)
                {
                    continue;
                }
                else
                {
                    return e;
                }

            }
            return null;
        }
    }

    private void resize(long oldMask, E[] oldBuffer, long pIndex, E e, Supplier<E> s)
    {
        assert (e != null && s == null) || (e == null || s != null);
        // oldBuffer 长度
        int newBufferLength = getNextBufferSize(oldBuffer);
        final E[] newBuffer;
        try
        {
            // 新 buffer 的长度和旧 buffer 的长度保持一致
            newBuffer = allocateRefArray(newBufferLength);
        }
        catch (OutOfMemoryError oom)
        {
            assert lvProducerIndex() == pIndex + 1;
            soProducerIndex(pIndex);
            throw oom;
        }
        // 用于扩容之后，指向扩容之后的新 buffer, procuder 直接向新 buffer 添加元素
        // consumerBuffer 还是指向旧的 buffer,继续消费没有被消费的元素
        // 读写分离
        producerBuffer = newBuffer;
        // 原始数组的 mask = （capacity - 1）<< 1
        // 原始数组的长度 = capacity + 1 , 多出的一个位置用于存放扩容之后新数组的指针
        // 所以新数组的 mask = (BufferLength - 2) << 1
        // 因为原数组的 capacity 必定是 2 的次幂，所以 mask 应该是 capacity - 1
        // 但由于相关 pIndex , cIndex 的步长为 2 （保证是偶数)，所以所有指针 maxCapacity，以及mask 都是真实的两倍
        // 而原数组指定的是 capacity，但真实的长度为 capacity + 1 , 多出的一个位置用于存放扩容之后新数组的指针
        // newBufferLength 获取的其实是 capacity + 1，已经 + 1 了，要保证2的次幂所以 newBufferLength - 1 计算 mask 的时候在 -1
        // 总共 -2
        final int newMask = (newBufferLength - 2) << 1;
        producerMask = newMask;

        // 原数组倒数第 2 的位置也就是 ProducerLimit 的位置用于存放 JUMP 标识，倒数第 1 的位置存放新数组的指针
        // 此时 pIndex 还是原来的 =  ProducerLimit，但是 producerIndex 已经 = ProducerLimit + 1 , 因为这里是扩容，producerIndex 已经 cas 加 1 更新了（低位置1 表示正在扩容）
        // 这里 oldMask 和 newMask 是相同的，所以计算出来的 offset 也是相同的都是 ProducerLimit 的位置（原数组倒数第2个位置）
        final long offsetInOld = modifiedCalcCircularRefElementOffset(pIndex, oldMask);
        final long offsetInNew = modifiedCalcCircularRefElementOffset(pIndex, newMask);

        // 将元素放入新数组中
        // offsetInOld = offsetInNew = 原数组中的 ProducerLimit 位置
        // 所以新元素 e 会被放置到 newBuffer 的 ProducerLimit 位置，这和我们想象中的是不一样的，并不是放在 newBuffer 的第一个位置
        // 但其实 mpsc 中的 buffer 是一个环形数组的设计，我们可以将环形数组的任意位置当做逻辑上的第一个位置（虽然实际上不是第一个位置）
        // newBuffer 逻辑上的第一个位置就是 oldBuffer 的 ProducerLimit， ProducerLimit + 2 位置原本释放下一个扩容后的数组
        // 但是此时 ProducerLimit + 2 已经变成逻辑上的第二个位置，下一个位置将进行回绕，第三个逻辑位置正好是 newBuffer[0] 。。。。
        // 直到回绕到 ProducerLimit - 2 的位置是 newBuffer 最后一个位置用于存放下一个扩容数组，ProducerLimit - 4 存放 JUMP
        // 记住环形数组是可以回绕的，不要把它当成是一个一维正常数组
        // 此时新元素 e 存储在 newBuffer[ProducerLimit >> 1] 的位置上，当 consumer 根据 cIndex 从 mpsc poll 元素的时候
        // 如果 cIndex = ProducerLimit，那么 consuemr 会发现 oldBuffer[ProducerLimit >> 1] 位置存储的是 JUMP
        // consumer 就会从 oldBuffer[ProducerLimit + 2 >> 1] 获取 newBuffer， 因为此时 cIndex 没有变,仍然等于 ProducerLimit
        // consumer 根据 cIndex 获取 newBuffer[ProducerLimit >> 1] 就获取到新元素了。consumer 的 poll 逻辑根据正常逻辑走就可以
        soRefElement(newBuffer, offsetInNew, e == null ? s.get() : e);// element in new array
        // 将扩容后新数组的指针放入到原数组的最后一个位置（mask+ 2）
        soRefElement(oldBuffer, nextArrayOffset(oldMask), newBuffer);// buffer linked

        // ASSERT code
        final long cIndex = lvConsumerIndex();
        final long availableInQueue = availableInQueue(pIndex, cIndex);
        RangeUtil.checkPositive(availableInQueue, "availableInQueue");

        // Invalidate racing CASs
        // We never set the limit beyond the bounds of a buffer

        // pIndex + 扩容后的可用空间
        soProducerLimit(pIndex + Math.min(newMask, availableInQueue));

        // make resize visible to the other producers
        soProducerIndex(pIndex + 2);// newBuffer 最后一个位置变成逻辑上的第二个位置，下一个位置将进行回绕 newBuffer[0]

        // INDEX visible before ELEMENT, consistent with consumer expectation

        // make resize visible to consumer
        // oldBuffer[ProducerLimit >> 1] 位置存储的是 JUMP
        // putOrderedObject 写之后，并不能保证其他线程立马读到
        // 需要同步机制，或者执行一次 volatile 写触发 store-load 屏障才能可见
        // 或者用 volatile 读的方式访问 buffer，否则 consumer 在 poll 方法中获取到的一直是 null 但其实是 JUMP(没看到)
        // see : https://github.com/netty/netty/issues/13137
        soRefElement(oldBuffer, offsetInOld, JUMP);
    }

    /**
     * @return next buffer size(inclusive of next array pointer)
     */
    protected abstract int getNextBufferSize(E[] buffer);

    /**
     * @return current buffer capacity for elements (excluding next pointer and jump entry) * 2
     */
    protected abstract long getCurrentBufferCapacity(long mask);
}
