//package com.github.peaquyen.xJournal.model
//
//import io.realm.kotlin.ext.realmListOf
//import io.realm.kotlin.types.RealmInstant
//import io.realm.kotlin.types.RealmList
//import io.realm.kotlin.types.RealmObject
//import org.mongodb.kbson.ObjectId
//import java.time.Instant
//
//open class Journal : RealmObject(){
//    var _id: ObjectId = ObjectId.invoke() // ObjectId is a class from Realm, create() deprecated
//    var ownerId: String = ""
//    var feeling: String = Feeling.neutral.name // Change Mood into Feeling
//    var title: String = ""
//    var description: String = ""
//    var images: RealmList<String> = realmListOf()
//    var date: RealmInstant = Instant.now().toRealmInstant() // Instant is a class from Realm,
//// now() deprecated
//}