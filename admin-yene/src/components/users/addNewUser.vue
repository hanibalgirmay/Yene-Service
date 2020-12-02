<template>
  <div>
    <md-button class="md-primary" @click="showDialog = true">Primary</md-button>

    <md-dialog :md-active.sync="showDialog">
      <md-dialog-title>Add Users</md-dialog-title>
      <md-tabs md-dynamic-height>
        <md-tab md-label="User">
          <form novalidate class="md-layout" @submit.prevent="validateUser">
            <md-card class="md-layout-item">
              <md-card-header>
                <div class="md-title">Users</div>
              </md-card-header>

              <md-card-content>
                <div class="md-layout md-gutter">
                  <div class="md-layout-item">
                    <md-field :class="getValidationClass('firstName')">
                      <label for="first-name">First Name</label>
                      <md-input
                        name="first-name"
                        id="first-name"
                        autocomplete="given-name"
                        v-model="form.firstName"
                        :disabled="sending"
                      />
                      <span class="md-error" v-if="!$v.form.firstName.required"
                        >The first name is required</span
                      >
                      <span
                        class="md-error"
                        v-else-if="!$v.form.firstName.minlength"
                        >Invalid first name</span
                      >
                    </md-field>
                  </div>

                  <div class="md-layout-item">
                    <md-field :class="getValidationClass('lastName')">
                      <label for="last-name">Last Name</label>
                      <md-input
                        name="last-name"
                        id="last-name"
                        autocomplete="family-name"
                        v-model="form.lastName"
                        :disabled="sending"
                      />
                      <span class="md-error" v-if="!$v.form.lastName.required"
                        >The last name is required</span
                      >
                      <span
                        class="md-error"
                        v-else-if="!$v.form.lastName.minlength"
                        >Invalid last name</span
                      >
                    </md-field>
                  </div>
                </div>

                <div class="md-layout md-gutter">
                  <div class="md-layout-item">
                    <md-switch v-model="isProvider"
                      >isProvider <small>(Default)</small></md-switch
                    >
                  </div>

                  <div class="md-layout-item">
                    <md-field :class="getValidationClass('password')">
                      <label for="password">Password</label>
                      <md-input
                        type="password"
                        id="password"
                        name="password"
                        v-model="form.password"
                        :disabled="sending"
                      />
                      <span class="md-error" v-if="!$v.form.password.required"
                        >The password is required</span
                      >
                      <span
                        class="md-error"
                        v-else-if="!$v.form.password.maxlength"
                        >Invalid password</span
                      >
                    </md-field>
                  </div>
                </div>

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
              </md-card-content>

              <md-progress-bar md-mode="indeterminate" v-if="sending" />

              <md-card-actions>
                <md-button type="submit" class="md-primary" :disabled="sending"
                  >Create user</md-button
                >
              </md-card-actions>
            </md-card>

            <md-snackbar :md-active.sync="userSaved"
              >The user {{ lastUser }} was saved with success!</md-snackbar
            >
          </form>
        </md-tab>
        <md-tab v-if="isProvider" md-label="Provider">
          <form>
            <md-field>
              <label>Type here!</label>
              <md-input v-model="type"></md-input>
              <span class="md-helper-text">Helper text</span>
            </md-field>
          </form>
        </md-tab>
      </md-tabs>

      <md-dialog-actions>
        <md-button class="md-primary" @click="showDialog = false"
          >Close</md-button
        >
        <md-button class="md-primary" @click="showDialog = false"
          >Save</md-button
        >
      </md-dialog-actions>
    </md-dialog>
  </div>
</template>

<script>
import { validationMixin } from "vuelidate";
import {
  required,
  email,
  minLength,
  maxLength,
} from "vuelidate/lib/validators";
import { usersCollection, auth } from "../../firebase.config";

export default {
  name: "AddUser",
  mixins: [validationMixin],
  data: () => ({
    showDialog: false,
    form: {
      firstName: null,
      lastName: null,
      isProvider: false,
      password: null,
      email: null,
    },
    userSaved: false,
    sending: false,
    lastUser: null,
  }),
  validations: {
    form: {
      firstName: {
        required,
        minLength: minLength(3),
      },
      lastName: {
        required,
        minLength: minLength(3),
      },
      password: {
        required,
        minLength: maxLength(6),
      },
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
      this.form.firstName = null;
      this.form.lastName = null;
      this.form.password = null;
      this.form.email = null;
    },
    saveUser() {
      this.sending = true;
      auth
        .createUserWithEmailAndPassword(this.form.email, this.form.password)
        .then((res) => {
          console.log("successfully register user", res.user);
          // String id = res.user.uid;
          this.registerUser(res.user.uid);
        })
        .catch((err) => {
          console.log("error", err.message);
        });
    },
    registerUser(id) {
      usersCollection.doc(id).set({
        firstName: this.form.firstName,
        lastName: this.form.lastName,
        email: this.form.email,
        isProvider: this.form.isProvider,
        receiveNews: true,
        image: "default",
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

<style></style>
