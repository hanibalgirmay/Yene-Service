<template>
  <div id="login">
    <form novalidate class="md-layout" @submit.prevent="validateUser">
      <md-card>
        <router-link to="/"><md-button>back</md-button></router-link>
        <md-card-header>
          <div class="md-title">Password Recovery</div>
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
        </md-card-content>

        <md-progress-bar md-mode="indeterminate" v-if="sending" />

        <md-card-actions>
          <md-button type="submit" class="md-primary" :disabled="sending"
            >Send Link</md-button
          >
        </md-card-actions>
      </md-card>
    </form>
  </div>
</template>

<script>
import { required, email } from "vuelidate/lib/validators";

import { auth } from "../firebase.config";

export default {
  name: "ForgotPassword",
  data: () => ({
    form: {
      email: null,
      err_msg: "",
    },
    sending: false,
    lastUser: null,
  }),
  validations: {
    form: {
      email: {
        required,
        email,
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
      this.form.email = null;
    },
    saveUser() {
      this.sending = true;
      // Instead of this timeout, here you can call your API
      auth
        .sendPasswordResetEmail(this.form.email)
        .then(() => {
          // localStorage.setItem("adminKey", data.displayNam);
          alert("link send");
          this.form.email = null;
          // this.$router.replace({ name: "/" });
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
