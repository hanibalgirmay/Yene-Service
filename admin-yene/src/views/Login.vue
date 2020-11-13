<template>
  <div id="login">
    <form novalidate class="md-layout" @submit.prevent="validateUser">
      <md-card>
        <md-card-header>
          <div class="md-title">Admin login</div>
        </md-card-header>
        <md-card-content>
          <p class="error">{{ err_msg }}</p>
          <div class="md-layout-item md-small-size-100">
            <md-field :class="getValidationClass('email')">
              <label for="email">Email</label>
              <md-input
                type="email"
                name="email"
                id="email"
                autocomplete="email"
                v-model="form.email"
                :disabled="sending"
              />
              <span class="md-error" v-if="!$v.form.email.required"
                >The email is required</span
              >
              <span class="md-error" v-else-if="!$v.form.email.email"
                >Invalid email</span
              >
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-100">
            <md-field :class="getValidationClass('password')">
              <label for="password">Password</label>
              <md-input
                name="password"
                id="password"
                autocomplete="given-name"
                v-model="form.password"
                :disabled="sending"
              />
              <span class="md-error" v-if="!$v.form.password.required"
                >Password is required</span
              >
              <span class="md-error" v-else-if="!$v.form.password.minlength"
                >Invalid password</span
              >
            </md-field>
          </div>
        </md-card-content>

        <md-progress-bar md-mode="indeterminate" v-if="sending" />

        <md-card-actions>
          <md-button type="submit" class="md-primary" :disabled="sending"
            >Login</md-button
          >
        </md-card-actions>
        <p style="margin-left:20px">Forgot password <router-link to="/forgot-password">click here</router-link></p>
      </md-card>
    </form>
  </div>
</template>

<script>
// import { validationMixin } from 'vuelidate'
import { required, email, minLength } from "vuelidate/lib/validators";

import { auth } from "../firebase.config";

export default {
  name: "Login",
  data: () => ({
    form: {
      email: null,
      password: null,
      err_msg: "",
    },
    userSaved: false,
    sending: false,
    lastUser: null,
  }),
  validations: {
    form: {
      email: {
        required,
        email,
      },
      password: {
        required,
        minLength: minLength(6),
      },
    },
  },
  methods: {
    getValidationClass(fieldName) {
      const field = this.$v.form[fieldName];

      if (field) {
        return {
          "md-invalid": field.$invalid && field.$dirty,
        };
      }
    },
    clearForm() {
      this.$v.$reset();
      this.form.password = null;
      this.form.email = null;
    },
    saveUser() {
      this.sending = true;
      // Instead of this timeout, here you can call your API
      auth
        .signInWithEmailAndPassword(this.form.email, this.form.password)
        .then((data) => {
          data.user
            .updateProfile({
              displayName: this.form.name,
            })
            .then(() => {
              localStorage.setItem("adminKey", data.displayNam);
              this.$router.replace({ name: "/dash" });
            });
        })
        .catch((err) => {
          this.err_msg = err.message;
          this.sending = false;
        });
    },
    validateUser() {
      this.$v.$touch();

      if (!this.$v.$invalid) {
        this.saveUser();
      }
    },
  },
};
</script>

<style>
.md-progress-bar {
  position: absolute;
  top: 0;
  right: 0;
  left: 0;
}
#login,
.md-layout {
  display: flex;
  align-items: center;
  align-content: center;
  min-height: 100vh;
  justify-items: center;
  align-self: center;
  justify-content: center;
}
.error {
  color: red;
}
</style>
