package skillima.screens.guild.model

sealed class  FetchGuildState{
    object Idle: FetchGuildState()
    object Loading : FetchGuildState()
    data class Success(val data:List<Guild>) : FetchGuildState()

    data class Error(val errorMessage: Int) : FetchGuildState()
}