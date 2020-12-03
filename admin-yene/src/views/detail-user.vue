<template>
  <section>
    <div class="md-layout md-gutter">
      <div class="md-layout-item md-medium-size-30">
        <md-card>
          <md-card-header>
            <div class="md-title">Profile</div>
          </md-card-header>
          <md-card-content>
            <md-avatar class="md-large">
              <img :src="userData.image" alt="People" />
            </md-avatar>
            <div class="data">
              <div class="info-user">
                <md-field>
                  <label>First Name</label>
                  <md-input v-model="userData.firstName"></md-input>
                </md-field>
                <md-field>
                  <label>Last Name</label>
                  <md-input v-model="userData.lastName"></md-input>
                </md-field>
                <md-field>
                  <label>PhoneNumber</label>
                  <md-input v-model="userData.phone"></md-input>
                </md-field>
              </div>
              <div class="info-provider">
                <md-field>
                  <label>Address</label>
                  <md-input :value="item.address"></md-input>
                </md-field>
                <md-field>
                  <label>City</label>
                  <md-input :value="item.city"></md-input>
                </md-field>
                <md-field>
                  <label>Gender</label>
                  <md-input :value="item.Gender"></md-input>
                </md-field>
                <div>
                  <label>Speciality</label>
                  <md-chips
                    :value="item.speciality"
                    md-placeholder="Add Speciality..."
                  ></md-chips>
                </div>
                <md-field>
                  <label>Type</label>
                  <md-input :value="item.type"></md-input>
                </md-field>
              </div>
            </div>
          </md-card-content>
        </md-card>
      </div>

      <!-- <div class="md-layout-item"></div> -->
    </div>
  </section>
</template>

<script>
import { usersCollection } from "../firebase.config";

export default {
  props: ["item"],
  data() {
    return { id: null, userData: [] };
  },
  methods: {
    getUser() {
      usersCollection
        .doc(this.id)
        .get()
        .then((res) => {
          if (res.exists) {
            this.userData = res.data();
          }
        });
    },
  },
  created() {
    this.id = this.$route.params["item"].user_id;
    console.log("id", this.id);

    if (this.id != "") {
      this.getUser();
    }
  },
};
</script>

<style>
section {
  min-height: 100vh;
}
.data {
  display: flex;
  flex-wrap: wrap;
}
</style>
