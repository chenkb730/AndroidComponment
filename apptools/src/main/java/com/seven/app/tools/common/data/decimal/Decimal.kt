package com.seven.app.tools.common.data.decimal

import java.lang.UnsupportedOperationException
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode


/**
 * 数值格式化
 * Created by Seven on 2018/1/22.
 */
internal class Decimal constructor(initValue: Any,
                                   private val mathContext: MathContext = defaultMathContext) : Number(), Cloneable {

    var bigDecimal: BigDecimal? = null
        private set

    init {
        init(initValue)
    }

    private fun init(initValue: Any?) {
        when (initValue) {
            is Decimal -> {
                val value = initValue as Decimal?
                bigDecimal = value!!.bigDecimal
            }
            is BigDecimal -> bigDecimal = initValue
            is BigInteger -> {
                val value = initValue as BigInteger?
                bigDecimal = BigDecimal(value, mathContext)
            }
            is Number -> {
                val value = initValue as Number?
                bigDecimal = BigDecimal(value!!.toString(), mathContext)
            }
            is String -> {
                val value = initValue as String?
                bigDecimal = BigDecimal(value, mathContext)
            }
            null -> bigDecimal = BigDecimal(0, mathContext)
            else -> throw UnsupportedOperationException("init Decimal default")
        }
    }

    /**
     * 获取该数被除后的整数
     *
     * @param object
     * 因数
     *
     * @return 结果
     */
    fun getDivGetInteger(`object`: Any): Decimal {
        return Decimal(this.bigDecimal!!.divideToIntegralValue(Decimal(`object`).bigDecimal, mathContext))
    }

    /**
     * 求余
     *
     * @param object
     * 因数
     *
     * @return 结果
     */
    fun getRemainder(`object`: Any): Decimal {
        return Decimal(this.bigDecimal!!.remainder(Decimal(`object`).bigDecimal, mathContext))
    }
    //endregion

    //region 基本数值运算

    /**
     * 基本数值运算：加法
     *
     * @param object
     *
     * @return
     */
    fun add(`object`: Any): Decimal {
        this.bigDecimal = this.bigDecimal!!.add(Decimal(`object`).bigDecimal, mathContext)
        return this
    }

    /**
     * 减法
     *
     * @param object
     *
     * @return
     */
    fun sub(`object`: Any): Decimal {
        this.bigDecimal = this.bigDecimal!!.subtract(Decimal(`object`).bigDecimal, mathContext)
        return this
    }

    /**
     * 乘法
     *
     * @param object
     *
     * @return
     */
    fun mul(`object`: Any): Decimal {
        this.bigDecimal = this.bigDecimal!!.multiply(Decimal(`object`).bigDecimal, mathContext)
        return this
    }

    /**
     * 除法
     *
     * @param object
     *
     * @return
     */
    operator fun div(`object`: Any): Decimal {
        this.bigDecimal = this.bigDecimal!!.divide(Decimal(`object`).bigDecimal, mathContext)
        return this
    }

    /**
     * 绝对值
     *
     * @return
     */
    fun abs(): Decimal {
        this.bigDecimal = this.bigDecimal!!.abs(mathContext)
        return this
    }
    //endregion

    //region 复杂数值运算

    /**
     * 幂运算
     *
     * @param n
     * 幂数
     *
     * @return 结果
     */
    fun pow(n: Int): Decimal {
        this.bigDecimal = this.bigDecimal!!.pow(n, mathContext)
        return this
    }

    /**
     * 开平方
     *
     * @param scale
     * 精度
     *
     * @return 结果
     */
    fun sqrt2(scale: Int): Decimal {
        if (this.bigDecimal!!.divide(BigDecimal(1), mathContext).toDouble() == 1.00 || this.bigDecimal!!.divide(BigDecimal(2), mathContext).toDouble() == 1.00) {
            this.bigDecimal = BigDecimal(toInt())
        }
        if (scale > 13) {
            this.bigDecimal = DecimalTool.sqrt(this.bigDecimal!!, scale, mathContext.roundingMode)
        } else {
            val sqrt = Math.sqrt(this.bigDecimal!!.toDouble())
            this.bigDecimal = BigDecimal(sqrt, mathContext)
        }
        return this
    }

    /**
     * 开N次方
     *
     * @param n
     * 几次方
     *
     * @return 结果
     */
    fun sqrtN(n: Int): Decimal {
        val log = Math.pow(this.toDouble(), 1.0 / n)
        this.bigDecimal = BigDecimal(log, mathContext)
        return this
    }

    override fun toString(): String {
        return fullStrValue()
    }

    fun fullStrValue(): String {
        return this.bigDecimal!!.toPlainString()
    }

    fun fullStrValue(scale: Int): String {
        return this.fullStrValue(scale, mathContext.roundingMode)
    }

    fun fullStrValue(scale: Int, roundingMode: RoundingMode): String {
        val decimalFormat = DecimalTool.scale2Format(scale, roundingMode)
        return decimalFormat.format(this.bigDecimal)
    }

    /**
     * 精确最多2位小数四舍五入转换为字符串
     *
     * @return
     */
    fun moneyStrValue(): String {
        return fullStrValue(2, this.mathContext.roundingMode)
    }

    /**
     * 精确最多2位小数四舍五入
     *
     * @return
     */
    fun moneyValue(): Double {
        return doubleValue(2, this.mathContext.roundingMode)
    }

    override fun toInt(): Int {
        return toDouble().toInt()
    }

    override fun toLong(): Long {
        return toDouble().toLong()
    }

    override fun toFloat(): Float {
        return toDouble().toFloat()
    }

    override fun toDouble(): Double {
        return this.bigDecimal!!.toDouble()
    }

    override fun toByte(): Byte {
        return toDouble().toByte()
    }

    override fun toChar(): Char {
        return toDouble().toChar()
    }

    override fun toShort(): Short {
        return toDouble().toShort()
    }

    /**
     * 传入进度和舍入原则进行double
     *
     * @param scale
     * 进度
     *
     * @return 结果
     */
    fun doubleValue(scale: Int): Double {
        return this.doubleValue(scale, mathContext.roundingMode)
    }

    /**
     * 传入进度和舍入原则进行double
     *
     * @param scale
     * 进度
     * @param roundingMode
     * 舍入原则
     *
     * @return 结果
     */
    fun doubleValue(scale: Int, roundingMode: RoundingMode): Double {
        val strValue = this.fullStrValue(scale, roundingMode)
        return java.lang.Double.valueOf(strValue)!!
    }
    //endregion


    override fun hashCode(): Int {
        return bigDecimal!!.hashCode()
    }

    override fun equals(obj: Any?): Boolean {
        return this.bigDecimal!! == Decimal(obj!!, MathContext.UNLIMITED).bigDecimal
    }

    public override fun clone(): Decimal {
        return Decimal(this.bigDecimal!!, this.mathContext)
    }

    companion object {
        @Volatile
        var defaultMathContext = MathContext.DECIMAL128

        /**
         * 便利生成方式,获取实例方法
         *
         * @param initValue
         *
         * @return
         */
        fun instance(initValue: Any): Decimal {
            return Decimal(initValue)
        }

        //region 便利生成方式,获取实例方法

        fun instance(initValue: Any, mathContext: MathContext): Decimal {
            return Decimal(initValue, mathContext)
        }
    }
}