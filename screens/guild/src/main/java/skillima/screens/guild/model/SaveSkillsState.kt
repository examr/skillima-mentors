package skillima.screens.guild.model

sealed class SaveSkillsState {
    object Idle : SaveSkillsState()
    object Loading : SaveSkillsState()
    object Success : SaveSkillsState()
    data class Error(val errorMessage: Int) : SaveSkillsState()
}