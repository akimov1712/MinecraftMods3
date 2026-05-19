package dev.akmvxx.feature.help

import androidx.annotation.StringRes
import dev.akmvxx.ui.R

data class FaqItem(
    val id: String,
    @StringRes val questionRes: Int,
    @StringRes val answerRes: Int,
)

internal val FaqItems: List<FaqItem> = listOf(
    FaqItem("account", R.string.faq_account_q, R.string.faq_account_a),
    FaqItem("need_internet", R.string.faq_need_internet_q, R.string.faq_need_internet_a),
    FaqItem("supported_versions", R.string.faq_supported_versions_q, R.string.faq_supported_versions_a),
    FaqItem("how_install", R.string.faq_how_install_q, R.string.faq_how_install_a),
    FaqItem("install_pack", R.string.faq_install_pack_q, R.string.faq_install_pack_a),
    FaqItem("install_fail", R.string.faq_install_fail_q, R.string.faq_install_fail_a),
    FaqItem("corrupted_file", R.string.faq_corrupted_file_q, R.string.faq_corrupted_file_a),
    FaqItem("mod_not_visible", R.string.faq_mod_not_visible_q, R.string.faq_mod_not_visible_a),
    FaqItem("experimental", R.string.faq_experimental_q, R.string.faq_experimental_a),
    FaqItem("lag", R.string.faq_lag_q, R.string.faq_lag_a),
    FaqItem("multiplayer", R.string.faq_multiplayer_q, R.string.faq_multiplayer_a),
    FaqItem("new_mods", R.string.faq_new_mods_q, R.string.faq_new_mods_a),
    FaqItem("updates", R.string.faq_updates_q, R.string.faq_updates_a),
    FaqItem("safety", R.string.faq_safety_q, R.string.faq_safety_a),
    FaqItem("ads", R.string.faq_ads_q, R.string.faq_ads_a),
    FaqItem("no_internet", R.string.faq_no_internet_q, R.string.faq_no_internet_a),
    FaqItem("uninstall", R.string.faq_uninstall_q, R.string.faq_uninstall_a),
    FaqItem("safe_for_saves", R.string.faq_safe_for_saves_q, R.string.faq_safe_for_saves_a),
    FaqItem("remove_mod", R.string.faq_remove_mod_q, R.string.faq_remove_mod_a),
    FaqItem("report_issue", R.string.faq_report_issue_q, R.string.faq_report_issue_a),
    FaqItem("submit_mod", R.string.faq_submit_mod_q, R.string.faq_submit_mod_a),
)

data class HelpState(
    val searchQuery: String = "",
    val expandedItemId: String? = null,
)

sealed interface HelpIntent {
    data class ChangeSearchQuery(val value: String) : HelpIntent
    data class ToggleExpand(val id: String) : HelpIntent
}

sealed interface HelpEvent
