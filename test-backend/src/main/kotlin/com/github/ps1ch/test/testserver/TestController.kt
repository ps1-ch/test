package com.github.ps1ch.test.testserver

import jakarta.mail.*
import org.springframework.context.annotation.Scope
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("api")
@Scope("session")
class TestController(
    val accountRepository: AccountRepository,
    val session: Session = Session.getInstance(Properties()),
    val store: MutableMap<String, Store> = mutableMapOf(),
) {
    @GetMapping("user")
    fun user(user: Principal): Principal {
        return user
    }

    @PostMapping("accounts")
    fun accounts(@AuthenticationPrincipal user: User): Iterable<Account> {
        return accountRepository.findAllByUser(user)
    }

//    @RequestMapping("connect")
//    fun connect(@AuthenticationPrincipal user: User): Iterable<Account> {
//        accountRepository.findAllByUser(user).apply {
//            forEach { setup(it) }
//            return this
//        }
//    }

    @PostMapping("login")
    fun login(
        @AuthenticationPrincipal user: User,
        @RequestParam accountName: String
    ): Account? {
        accountRepository.findByUserAndName(user, accountName).apply {
            this?.let {
                setup(it)
            }
            return this
        }
    }

    @PostMapping("select")
    fun select(
        @AuthenticationPrincipal user: User,
        @RequestParam accountName: String,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): List<RenderedMessage> {
        store[accountName]?.getFolder("INBOX").let { folder ->
            folder?.open(Folder.READ_ONLY)
            folder?.messages?.copyOfRange(page * size - size, page * size).let { messages ->
                folder?.fetch(messages, FetchProfile().apply { add(FetchProfile.Item.ENVELOPE) })
                folder?.close()
                return messages?.map { it.render() }.orEmpty()
            }
        }
    }

    fun setup(account: Account) {
        store[account.name] = session.getStore("imaps")
        store[account.name]?.connect(account.inbound)
    }

    fun Store.connect(inbound: Inbound) = connect(inbound.host, inbound.username, inbound.password)
    fun Message.render() = RenderedMessage(messageNumber, from[0].toString(), subject, receivedDate, size)

    data class RenderedMessage(
        val messageNumber: Int,
        val from: String,
        val subject: String,
        val receivedDate: Date,
        val size: Int
    )
}
