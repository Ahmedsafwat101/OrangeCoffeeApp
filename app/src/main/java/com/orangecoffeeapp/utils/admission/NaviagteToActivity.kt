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
            UserTypes.Customer -> intent = Intent(activity, CustomerHomeActivity::class.java)
            UserTypes.Owner -> intent = Intent(activity, OwnerHomeActivity::class.java)
            UserTypes.Admin -> intent = Intent(activity, AdminMainActivity::class.java)
        }

        return intent

    }
}