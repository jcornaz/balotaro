package balotenketo.balotaro.auth

import com.stormpath.sdk.account.Account
import com.stormpath.sdk.servlet.account.AccountResolver
import javax.servlet.http.HttpServletRequest

object StormPath {

    fun getAccount(request: HttpServletRequest): Account? =
            AccountResolver.INSTANCE.getAccount(request)

    fun getRequiredAccount(request: HttpServletRequest): Account =
            AccountResolver.INSTANCE.getRequiredAccount(request)
}