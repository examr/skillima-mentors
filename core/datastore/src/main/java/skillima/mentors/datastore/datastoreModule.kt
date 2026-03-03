package skillima.mentors.datastore

import org.koin.dsl.module

val datastoreModule = module {
    single { DatastoreHelper(get()) }
}