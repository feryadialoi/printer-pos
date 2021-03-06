package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils

import android.graphics.Bitmap
import android.util.Log

object ByteArrayUtil {

    private var UNICODE_TEXT = byteArrayOf(
        0x23, 0x23, 0x23,
        0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23,
        0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23,
        0x23, 0x23, 0x23
    )

    private var binaryArray = arrayOf(
        "0000", "0001", "0010", "0011",
        "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
        "1100", "1101", "1110", "1111"
    )

    private const val hexStr = "0123456789ABCDEF"

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val bitmapHeight = bitmap.height
        val bitmapWidth = bitmap.width

        val list = ArrayList<String>()
        var sb: StringBuffer

        var bitLength = bitmapWidth / 8
        val zeroCount = bitmapWidth % 8

        var zeroStr = ""
        if (zeroCount > 0) {
            bitLength = bitmapWidth / 8 + 1
            for (i in 0 until 8 - zeroCount) {
                zeroStr += "0"
            }
        }

        for (i in 0 until bitmapHeight) {
            sb = StringBuffer()
            for (j in 0 until bitmapWidth) {
                val color = bitmap.getPixel(j, i)

                val r = color shr 16 and 0xff
                val g = color shr 8 and 0xff
                val b = color and 0xff

                // if color close to white，bit='0', else bit='1'
                if (r > 160 && g > 160 && b > 160) {
                    sb.append("0")

                } else {
                    sb.append("1")
                }
            }

            if ( zeroCount > 0) {
                sb.append(zeroStr)
            }

            list.add(sb.toString())
        }
        val bitmapHexList = binaryListToHexStringList(list)
        val commandHexString = "1D763000"
        var widthHexString = Integer
            .toHexString(if (bitmapWidth % 8 == 0) bitmapWidth / 8 else bitmapWidth / 8 + 1)
        if (widthHexString.length > 2) {
            Log.e("decodeBitmap error", " width is too large")
//                return null;
        } else if (widthHexString.length == 1) {
            widthHexString = "0$widthHexString"
        }

        widthHexString += "00"

        var heightHexString = Integer.toHexString(bitmapHeight)
        if (heightHexString.length > 2) {
            Log.e("decodeBitmap error", " height is too large")
//                return null
        } else if (heightHexString.length == 1) {
            heightHexString = "0$heightHexString"
        }

        heightHexString += "00"

        val commandList = ArrayList<String>()
        commandList.add(commandHexString + widthHexString + heightHexString)
        commandList.addAll(bitmapHexList)

        return hexListToByte(commandList)
    }

    private fun hexListToByte(list: List<String>): ByteArray {
        val commandList = ArrayList<ByteArray>()
        for (hexStr in list) {
            commandList.add(hexStringToBytes(hexStr))
        }
        return sysCopy(commandList)
    }

    private fun hexStringToBytes(hexString: String): ByteArray {

        val newHexString = hexString!!.toUpperCase()
        val length = newHexString.length / 2
        val hexChars = newHexString.toCharArray()
        val d = ByteArray(length)

        for (i in 0 until length) {
            val pos = i * 2
            d[i] = (charToByte(hexChars[pos]).toInt() shl 4 or charToByte(hexChars[pos + 1]).toInt()).toByte()
        }
        return d
    }

    private fun charToByte(c: Char): Byte {
        return "0123456789ABCDEF".indexOf(c).toByte()
    }

    private fun sysCopy(srcArrays: List<ByteArray>): ByteArray {
        var len = 0
        for (srcArray in srcArrays) {
            len += srcArray.size
        }
        val destArray = ByteArray(len)
        var destLen = 0
        for (srcArray in srcArrays) {
            System.arraycopy(srcArray, 0, destArray, destLen, srcArray.size)
            destLen += srcArray.size
        }
        return destArray
    }

    private fun binaryListToHexStringList(list: List<String>): ArrayList<String> {
        val hexList = ArrayList<String>()
        for (binaryStr in list) {
            val sb = StringBuffer()
            var i = 0
            while (i < binaryStr.length) {
                val str = binaryStr.substring(i, i + 8)
                val hexString: String = binaryStrToHexString(str)
                sb.append(hexString)
                i += 8
            }
            hexList.add(sb.toString())
        }
        return hexList
    }

    private fun binaryStrToHexString(binaryStr: String): String {
        var hex = ""
        val f4 = binaryStr.substring(0, 4)
        val b4 = binaryStr.substring(4, 8)
        for (i in binaryArray.indices) {
            if (f4 == binaryArray[i]) hex += hexStr.substring(i, i + 1)
        }
        for (i in binaryArray.indices) {
            if (b4 == binaryArray[i]) hex += hexStr.substring(i, i + 1)
        }

        return hex
    }

}