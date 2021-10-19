package com.orangecoffeeapp.utils.admission

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.orangecoffeeapp.constants.UserTypes
import com.orangecoffeeapp.ui.admin.AdminMainActivity
import com.orangecoffeeapp.ui.customer.CustomerHomeActivity
import com.orangecoffeeapp.ui.owner.OwnerHomeActivity

object NavigateToActivity {
     fun moveToHomeActivity(type:String,activity:Context): Intent? {
        var intent: Intent? = null
        when(type){

            UserTypes.Customer -> {
                intent = Intent(activity, CustomerHomeActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            UserTypes.Owner -> {intent = Intent(activity, OwnerHomeActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            UserTypes.Admin ->{ intent = Intent(activity, AdminMainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        }

        return intent

    }
}