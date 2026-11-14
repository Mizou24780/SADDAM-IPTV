
package com.ipvio.stream

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

class AccentPreset(val name: String, val fill: Int, val highlight: Int)

class LookPalette(
    val bg: Color,
    val surf: Color,
    val surf2: Color,
    val fill: Color,
    val high: Color,
    val onFill: Color
)

class BgPreset(val name: String, val bg: Int, val surf: Int, val surf2: Int)

object Look {

    // SADDAM IPTV - Official Brand Colors
    const val BRAND_PURPLE = 0x8B5CF6
    const val BRAND_PURPLE_HI = 0xC4B5FD
    const val BRAND_NAVY = 0x09090F

    const val DEFAULT_ACCENT = 0
    const val DEFAULT_BG = 0

    val ACCENTS = listOf(
        AccentPreset("SADDAM IPTV", BRAND_PURPLE, BRAND_PURPLE_HI),
        AccentPreset("Violet Neon", 0x7C3AED, 0xC4B5FD),
        AccentPreset("Ocean Blue", 0x2563EB, 0x7DD3FC),
        AccentPreset("Emerald", 0x047857, 0x6EE7B7),
        AccentPreset("Sunset", 0xC2414B, 0xFDBA74),
        AccentPreset("Rose", 0xBE185D, 0xFDA4AF),
        AccentPreset("Gold", 0xA16207, 0xFDE68A),
        AccentPreset("Orange", 0xC2410C, 0xFDBA74),
        AccentPreset("Ice Blue", 0x0E7490, 0x67E8F9),
        AccentPreset("Lime", 0x4D7C0F, 0xBEF264),
        AccentPreset("Purple", 0x7E22CE, 0xE879F9),
        AccentPreset("Silver", 0x475569, 0xE2E8F0)
    )

    val SWATCHES = listOf(
        0xE53935, 0xF4511E, 0xFB8C00, 0xFFB300, 0xFDD835,
        0x9CCC65, 0x4ADE80, 0x00C98D, 0x00BFA5, 0x26C6DA,
        0x29B6F6, 0x3D7BFF, 0x5C6BFF, 0x8E5CFF, 0xB44CF0,
        0xE040C0, 0xFF4F8B, 0xFF6F61, 0xCFD8DC, 0x78909C
    )

    val CUSTOM get() = ACCENTS.size

    val BGS = listOf(
        BgPreset("SADDAM Dark", BRAND_NAVY, 0x151522, 0x202033),
        BgPreset("OLED Black", 0x000000, 0x0B0C10, 0x15171E),
        BgPreset("Midnight", 0x07080C, 0x12141C, 0x1B1E2A),
        BgPreset("Graphite", 0x121316, 0x1C1E24, 0x272A32),
        BgPreset("Deep Blue", 0x070B14, 0x101828, 0x1A2540),
        BgPreset("Vampire", 0x282A36, 0x343746, 0x44475A),
        BgPreset("Ice", 0x2E3440, 0x3B4252, 0x4C566A),
        BgPreset("Deep Sea", 0x06161F, 0x0D2530, 0x143546),
        BgPreset("Forest", 0x0C1510, 0x16241B, 0x203428),
        BgPreset("Ember", 0x1A110C, 0x2A1B13, 0x3A2619)
    )

    class ReadyDesign(val name: String, val accent: Int, val bg: Int)

    val DESIGNS = listOf(
        ReadyDesign("SADDAM IPTV", 0, 0),
        ReadyDesign("Purple Night", 10, 1),
        ReadyDesign("Violet Neon", 1, 2),
        ReadyDesign("Ocean", 2, 4),
        ReadyDesign("Emerald", 3, 8),
        ReadyDesign("Golden Night", 6, 1),
        ReadyDesign("Sunset", 4, 9)
    )

    const val DEFAULT_SCALE = 1

    val MOTION = listOf("Aus", "Dezent", "Normal")
    val SCALES = listOf("Automatisch", "85 %", "100 %", "115 %", "130 %")

    var accentIdx by mutableIntStateOf(DEFAULT_ACCENT)
    var custom by mutableIntStateOf(BRAND_PURPLE)
    var custom2 by mutableIntStateOf(-1)
    var bgIdx by mutableIntStateOf(DEFAULT_BG)
    var motion by mutableIntStateOf(2)
    var uiScale by mutableIntStateOf(DEFAULT_SCALE)

    private fun bgRgb(): Int =
        BGS[bgIdx.coerceIn(0, BGS.lastIndex)].bg

    fun fillRgb(): Int =
        if (accentIdx in ACCENTS.indices) ACCENTS[accentIdx].fill else custom

    fun autoHighRgb(): Int =
        ThemeMath.accentFromRgb(custom, bgRgb()).second

    fun highRgb(): Int = when {
        accentIdx in ACCENTS.indices -> ACCENTS[accentIdx].highlight
        custom2 >= 0 -> custom2
        else -> autoHighRgb()
    }

    private fun c(rgb: Int) =
        Color(0xFF000000.toInt() or rgb)

    val bg: Color get() = c(BGS[bgIdx.coerceIn(0, BGS.lastIndex)].bg)
    val surf: Color get() = c(BGS[bgIdx.coerceIn(0, BGS.lastIndex)].surf)
    val surf2: Color get() = c(BGS[bgIdx.coerceIn(0, BGS.lastIndex)].surf2)

    val accent1: Color get() = c(fillRgb())
    val accent2: Color get() = c(highRgb())
    val onAccent: Color get() = c(ThemeMath.onAccent(fillRgb()))

    fun palette(
        accent: Int = accentIdx,
        cust: Int = custom,
        cust2: Int = custom2,
        bgI: Int = bgIdx
    ): LookPalette {
        val b = BGS[bgI.coerceIn(0, BGS.lastIndex)]

        val fill = if (accent in ACCENTS.indices)
            ACCENTS[accent].fill else cust

        val high = when {
            accent in ACCENTS.indices -> ACCENTS[accent].highlight
            cust2 >= 0 -> cust2
            else -> ThemeMath.accentFromRgb(cust, b.bg).second
        }

        return LookPalette(
            c(b.bg),
            c(b.surf),
            c(b.surf2),
            c(fill),
            c(high),
            c(ThemeMath.onAccent(fill))
        )
    }

    fun reset() {
        accentIdx = DEFAULT_ACCENT
        custom = BRAND_PURPLE
        custom2 = -1
        bgIdx = DEFAULT_BG
        motion = 2
        uiScale = DEFAULT_SCALE
    }
}

fun focusScale(target: Float): Float =
    ThemeMath.focusScale(target, Look.motion)
