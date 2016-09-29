package balotenketo.balotaro.auth

import com.stormpath.sdk.account.Account
import com.stormpath.sdk.client.Clients
import com.stormpath.sdk.servlet.account.AccountResolver
import com.stormpath.sdk.servlet.application.ApplicationResolver
import javax.servlet.http.HttpServletRequest

object StormPath {

    val client by lazy { Clients.builder().build() }

    fun createAccount(request: HttpServletRequest, email: String, password: String): Account =
            ApplicationResolver.INSTANCE.getApplication(request).createAccount(
                    client.instantiate(Account::class.java).apply {
                        setEmail(email)
                        setPassword(password)
                    }
            )


    fun getAccount(request: HttpServletRequest): Account? =
            AccountResolver.INSTANCE.getAccount(request)

    fun getRequiredAccount(request: HttpServletRequest): Account =
            AccountResolver.INSTANCE.getRequiredAccount(request)
}