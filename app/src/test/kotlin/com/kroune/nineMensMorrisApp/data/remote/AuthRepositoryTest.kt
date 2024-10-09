package com.kroune.nineMensMorrisApp.data.remote

import com.kroune.nineMensMorrisApp.data.remote.auth.AuthRepositoryImpl
import org.junit.jupiter.api.Test

class AuthRepositoryTest {
    @Test
    fun loginNameValidator() {
        val parent = AuthRepositoryImpl()
        // invalid
        run {
            // length
            assert(!parent.loginValidator(""))
            assert(!parent.loginValidator("a"))
            assert(!parent.loginValidator("sn"))
            assert(!parent.loginValidator("sn3"))
            assert(!parent.loginValidator("3034"))
            // illegal symbols
            assert(!parent.loginValidator("@@@q@@@"))
            assert(!parent.loginValidator("anc!!!!!"))
            assert(!parent.loginValidator("name withSpace"))
            // too long
            assert(!parent.loginValidator("veryVeryVeryLongName"))
        }
        // valid
        run {
            assert(parent.loginValidator("someLongName"))
            assert(parent.loginValidator("anotherValid"))
            assert(parent.loginValidator("corRectName"))
            assert(parent.loginValidator("120player"))
            assert(parent.loginValidator("hhahaha0"))
        }
    }


    @Test
    fun commonPasswordValidator() {
        val parent = AuthRepositoryImpl()
        assert(!parent.passwordValidator(""))
        assert(!parent.passwordValidator("a"))
        assert(!parent.passwordValidator("sn"))
        assert(!parent.passwordValidator("sn3"))
        assert(!parent.passwordValidator("3034"))
        // illegal symbols
        assert(!parent.passwordValidator("@@@q@@@"))
        assert(!parent.passwordValidator("anc!!!!!"))
        assert(!parent.passwordValidator("name withSpace"))
        // too long
        assert(!parent.passwordValidator("veryVeryVeryLongName"))
        // no numbers
        assert(!parent.passwordValidator("anotherValid"))
        assert(!parent.passwordValidator("corRectName"))
        // valid
        run {
            assert(parent.passwordValidator("120player"))
            assert(parent.passwordValidator("hhahaha0"))
            assert(parent.passwordValidator("somePassword1"))
        }
    }
}
