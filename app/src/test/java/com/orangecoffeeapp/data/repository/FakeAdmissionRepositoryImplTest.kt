package com.orangecoffeeapp.data.repository

import com.orangecoffeeapp.data.models.LoginFormModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.utils.Hashing
import org.junit.Assert.*

class FakeAdmissionRepositoryImplTest : AdmissionRepository {

    private var fakeDB = mutableMapOf<String, UserModel>(
        Hashing.sha256("ahmedsafwat@gmail.com").toString() to UserModel(
            "Ahmed",
            "Safwat",
            "ahmedsafwat@gmail.com",
            "123456789",
            "01126638220",
            "Customer",
            true,
        ),
        Hashing.sha256("ahmedsafwat2@gmail.com").toString() to UserModel(
            "Ahmed",
            "Safwat",
            "ahmedsafwat2@gmail.com",
            "123456789",
            "01126638220",
            "Customer",
            false,
        )
    )

    override suspend fun validateUserLogin(user: LoginFormModel): UserModel? {
        val dbKey = Hashing.sha256(user.email).toString() // hashed email
        if (fakeDB.containsKey(dbKey)) {
            return fakeDB.getValue(dbKey)
        }
        return null
    }

    override suspend fun validateUserSignUp(user: UserModel): UserModel? {
        val dbKey = Hashing.sha256(user.email).toString() // hashed email
        if (!fakeDB.containsKey(dbKey)) {
            fakeDB[dbKey] = user
            return user
        }
        return null
    }

    override suspend fun updateUser(user: UserModel): UserModel? {
        val dbKey = Hashing.sha256(user.email).toString() // hashed email
        if (fakeDB.containsKey(dbKey)) {
            fakeDB[dbKey] = user

            return user
        }
        return null
    }

}