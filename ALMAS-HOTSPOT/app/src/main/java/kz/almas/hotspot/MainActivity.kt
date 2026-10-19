package kz.almas.hotspot

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.content.Context
import android.view.Gravity
import android.widget.*
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : Activity() {
    private val bg = Color.rgb(5, 16, 31)
    private val panel = Color.rgb(12, 33, 53)
    private val cyan = Color.rgb(0, 220, 225)
    private val green = Color.rgb(0, 231, 155)
    private lateinit var status: TextView
    private lateinit var root: TextView
    private lateinit var network: TextView

    private fun card(text: String, color: Int = Color.WHITE): TextView {
        val view = TextView(this)
        view.text = text
        view.textSize = 17f
        view.setTextColor(color)
        view.setPadding(28, 28, 28, 28)
        view.gravity = Gravity.CENTER_VERTICAL
        view.background = GradientDrawable().apply {
            setColor(panel); cornerRadius = 28f; setStroke(2, Color.rgb(17, 79, 103))
        }
        return view
    }

    private fun add(parent: LinearLayout, view: TextView) {
        val p = LinearLayout.LayoutParams(-1, -2)
        p.bottomMargin = 20
        parent.addView(view, p)
    }

    private fun hasRoot(): Boolean = try {
        val p = ProcessBuilder("su", "-c", "id").redirectErrorStream(true).start()
        val output = BufferedReader(InputStreamReader(p.inputStream)).readText()
        p.waitFor() == 0 && output.contains("uid=0")
    } catch (_: Exception) { false }

    private fun connection(): String {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val caps = cm.getNetworkCapabilities(cm.activeNetwork) ?: return "Интернет жоқ"
        return when {
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Ұялы интернет қосылған"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi қосылған"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "VPN желісі"
            else -> "Желі қосылған"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        val scroll = ScrollView(this).apply { setBackgroundColor(bg) }
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 50, 28, 32)
        }
        scroll.addView(layout)
        val title = card("◈  ALMAS HOTSPOT", cyan).apply {
            textSize = 26f; gravity = Gravity.CENTER; setTypeface(null, Typeface.BOLD)
        }
        add(layout, title)
        network = card("Желі тексерілуде...", green); add(layout, network)
        root = card("ROOT тексерілуде...", cyan); add(layout, root)
        status = card("HOTSPOT: басқару модулі әзірленуде"); add(layout, status)
        add(layout, card("VPN: әзірге қосылмаған"))
        add(layout, card("DNS: жүйелік параметрлер"))
        val refresh = Button(this).apply {
            text = "ЖЕЛІНІ ТЕКСЕРУ"
            setOnClickListener { refreshStatus() }
        }
        layout.addView(refresh, LinearLayout.LayoutParams(-1, -2))
        add(layout, card("Қауіпсіздік: бұл нұсқа жүйелік желі баптауларын өзгертпейді.", cyan))
        setContentView(scroll)
        refreshStatus()
    }

    private fun refreshStatus() {
        network.text = "ЖЕЛІ: ${connection()}"
        root.text = if (hasRoot()) "ROOT: ҚОСЫЛҒАН ✓" else "ROOT: қолжетімсіз"
        status.text = "HOTSPOT: басқару келесі нұсқада"
    }
}
