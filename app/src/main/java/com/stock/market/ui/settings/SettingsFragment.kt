package com.stock.market.ui.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.stock.market.databinding.FragmentSettingsBinding

/**
 * 设置页面
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val DEVELOPER_WECHAT = "lcccc9897"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 开发者信息
        binding.tvDeveloperContact.text = DEVELOPER_WECHAT

        // 版本信息
        binding.tvVersion.text = "v1.0.0"

        // 点击复制微信号
        binding.layoutDeveloper.setOnClickListener {
            copyToClipboard(DEVELOPER_WECHAT)
            Toast.makeText(requireContext(), "微信号已复制：$DEVELOPER_WECHAT", Toast.LENGTH_SHORT).show()
        }

        // 免责声明
        binding.tvDisclaimer.text =
            "本程序展示的公开查询数据、仅供参考，不构成任何投资建议。\n" +
            "股市有风险，投资需谨慎。"
    }

    /**
     * 复制文本到剪贴板
     */
    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("微信号", text)
        clipboard.setPrimaryClip(clip)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
