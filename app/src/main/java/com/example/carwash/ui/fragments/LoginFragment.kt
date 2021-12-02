package com.example.carwash.ui.fragments


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.carwash.R
import com.example.carwash.databinding.FragmentLoginBinding
import com.example.carwash.util.DialogProgress
import kotlinx.android.synthetic.main.fragment_login.*
import com.example.carwash.util.Util
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment(), View.OnClickListener {

    private var auth: FirebaseAuth? = null

    private lateinit var loginBinding: FragmentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        navigateToHome()
        createAccount()

        return loginBinding.root
    }

    private fun navigateToHome() {

        loginBinding.btnLoginChangeAccount.setOnClickListener(this)
        loginBinding.tvCreateAccount.setOnClickListener(this)
    }

    private fun createAccount() {
        loginBinding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.nav_frag_login_to_cadastrar_user)
        }
    }


    companion object {
        const val TAG = "LoginFragment"
    }

    override fun onClick(p0: View?) {
        auth = Firebase.auth

        when (p0?.id) {

            btnLoginChangeAccount.id -> {
                buttonLogin()
            }
        }
    }


    @SuppressLint("ResourceAsColor")
    private fun buttonLogin() {

        val email = etEmailChangeAccount.text.toString()
        val password = etPasswordChangeAccount.text.toString()

        //trim remove os espaços do input
        if (!email.trim().equals("") && !password.trim().equals("")) {
            btnLoginChangeAccount.setOnClickListener {
              //  btnLoginChangeAccount.setBackgroundResource(R.color.white)

                if (Util.statusInternet(requireContext())) {
                    login(email, password)
                } else {
                    Util.exibirToast(requireContext(), "Sem conexão com a internet")
                }
            }
        } else {
            Util.exibirToast(requireContext(), "campo em branco")
        }

    }

    fun login(email: String, password: String) {

        val dialogProgress = DialogProgress()
        dialogProgress.show(parentFragmentManager,"1")

        auth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->

            dialogProgress.dismiss()

            if (task.isSuccessful) {

                Util.exibirToast(requireContext(), "Sucesso ao logar")
                findNavController().navigate(R.id.nav_frag_login_to_home)


            } else {

                //Util.exibirToast(requireContext(), "Usuário ou senha inválido ${task.exception.toString()}")

                val erro = task.exception.toString()

                errorsFirebase(erro)

                Log.d("logcat",task.exception.toString())

            }

        }

    }

    fun errorsFirebase(erro:String){

        if(erro.contains("There is no user record corresponding to this identifier. The user may have been deleted." )){
            Util.exibirToast(requireContext(),"E-mail não cadastrado")
        }else if(erro.contains("The password is invalid or the user does not have a password.")){
            Util.exibirToast(requireContext(),"Usuário ou Senha inválida")
        }else if(erro.contains("The password is invalid or the user does not have a password.")) {
            Util.exibirToast(requireContext(), "Este e-mail não é válido")
        }else{
            Util.exibirToast(requireContext(), "Ocorreu um erro inesperado")
        }

    }

}

