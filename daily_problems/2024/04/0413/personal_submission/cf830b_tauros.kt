inline fun findFirst(n: Int, judge: (Int) -> Boolean): Int {
    var l = 0
    var r = n
    while (l < r) {
        val mid = l + (r - l) / 2
        if (judge(mid)) r = mid
        else l = mid + 1
    }
    return l
}
typealias int = Int
inline fun iar(size: Int, init: (Int) -> Int = { 0 }) = IntArray(size) { init(it) }
inline fun <reified T> mlo(vararg elements: T) = mutableListOf(*elements)
inline fun <reified K, reified V> mmo(vararg pairs: Pair<K, V>) = mutableMapOf(*pairs)
inline fun <R> IntArray.bitQuery(pos: Int, initial: R, operation: (R, Int) -> R): R {
    var iter = pos
    var ans = initial
    while (iter > 0) {
        ans = operation(ans, this[iter])
        iter -= iter.takeLowestOneBit()
    }
    return ans
}
inline fun IntArray.bitUpdateWithIndex(pos: Int, calc: IntArray.(Int) -> Unit) {
    if (pos <= 0) return
    var iter = pos
    while (iter < size) {
        calc(iter)
        iter += iter.takeLowestOneBit()
    }
}

/**
 * generated by kotlincputil@tauros
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/830/B
    // 模拟实现
    val n = readln().toInt()
    val nums = readln().split(" ").map { it.toInt() }.toIntArray()

    val pos = mmo<int, MutableList<int>>()
    for (i in 0 until n) {
        pos.computeIfAbsent(nums[i]) { mlo() }.add(i)
    }
    val list = pos.toList().sortedBy { it.first }.map { it.second.toIntArray() }.toTypedArray()

    fun IntArray.update(pos: int) = this.bitUpdateWithIndex(pos) { this[it] += 1 }
    fun IntArray.query(pos: int) = this.bitQuery(pos, 0, int::plus)

    val bit = iar(n + 1)
    var ans = 0L
    fun transfer(from: int, to: int) {
        val res = if (to >= from) {
            val deleted = bit.query(to + 1) - bit.query(from + 1)
            to - from - deleted
        } else {
            val deleted = bit.query(n) - bit.query(from + 1) + bit.query(to + 1)
            n - (from - to) - deleted
        }
        bit.update(to + 1)
        ans += res
    }
    transfer(-1, list[0][0])
    var (numi, posi) = 0 to 0
    var pre = list[0][0]
    while (true) {
        for (j in posi + 1 until list[numi].size) {
            val next = list[numi][j]
            transfer(pre, next); pre = next
        }
        for (j in 0 until posi) {
            val next = list[numi][j]
            transfer(pre, next); pre = next
        }
        if (numi == list.size - 1) break
        val posj = findFirst(list[numi + 1].size) { list[numi + 1][it] > pre }
        numi += 1; posi = if (posj == list[numi].size) 0 else posj
        transfer(pre, list[numi][posi]); pre = list[numi][posi]
    }
    println(ans)
}