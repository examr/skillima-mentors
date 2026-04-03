package skillima.data.guild.di

import org.koin.dsl.module
import skillima.data.guild.repository.GuildRepository
import skillima.data.guild.repository.GuildRepositoryImpl

val guildDataModule = module {
    single<GuildRepository>{
        GuildRepositoryImpl(get())
    }
}