<template>
  <div>
    <md-table
      id="table"
      v-model="providersData"
      md-sort="speciality"
      md-sort-order="asc"
      md-card
      md-fixed-header
    >
      <md-table-toolbar>
        <div class="md-toolbar-section-start">
          <h1 class="md-title">Users</h1>
        </div>

        <md-field md-clearable class="md-toolbar-section-end">
          <md-input
            placeholder="Search by name..."
            v-model="search"
            @input="searchOnTable"
          />
        </md-field>
      </md-table-toolbar>

      <md-table-empty-state
        md-label="No users found"
        :md-description="
          `No user found for this '${search}' query. Try a different search term or create a new user.`
        "
      >
        <md-button class="md-primary md-raised" @click="newUser"
          >Create New User</md-button
        >
      </md-table-empty-state>

      <md-table-row
        @click="detail(item)"
        slot="md-table-row"
        slot-scope="{ item }"
      >
        <!-- <md-table-cell md-label="ID" md-sort-by="id" md-numeric>{{
          item.id
        }}</md-table-cell> -->
        <md-table-cell md-label="City" md-sort-by="city">{{
          item.city
        }}</md-table-cell>
        <md-table-cell md-label="Experience" md-sort-by="experience">{{
          item.experience
        }}</md-table-cell>
        <md-table-cell md-label="Gender" md-sort-by="gender">{{
          item.gender
        }}</md-table-cell>
        <md-table-cell md-label="Status" md-sort-by="status">
          <span class="statusBtn">active</span>
        </md-table-cell>
        <md-table-cell md-label="Speciality" md-sort-by="speciality">{{
          item.speciality
        }}</md-table-cell>
      </md-table-row>
    </md-table>
  </div>
</template>

<script>
// import
import { providerCollection } from "../../firebase.config";

const toLower = (text) => {
  return text.toString().toLowerCase();
};

const searchByName = (items, term) => {
  if (term) {
    return items.filter((item) => toLower(item.name).includes(toLower(term)));
  }

  return items;
};
export default {
  name: "ProvidersTable",
  data() {
    return {
      providersData: [],
      listSpecial: [],
      search: null,
    };
  },
  methods: {
    newUser() {
      window.alert("Noop");
    },
    searchOnTable() {
      this.searched = searchByName(this.users, this.search);
    },
    getProviderUser() {
      this.providersData = [];
      providerCollection
        .get()
        .then((snap) => {
          snap.forEach((doc) => {
            // this.providersData.push({
            //   id: doc.id,
            //   gender: doc.data().Gender,
            //   aboutMe: doc.data().about_me,
            //   address: doc.data().address,
            //   certifiedCertficate: doc.data().certifiedCertficate,
            //   city: doc.data().city,
            //   experience: doc.data().experience,
            //   speciality: doc.data().speciality,
            //   userInfo: doc.data().userInfo,
            //   locationInfo: doc.data().locationId,
            // });
            this.providersData.push(doc.data());
            console.log("user info: ", doc.data().userInfo);
          });
        })
        .catch((err) => {
          console.log("error occur: ", err);
        });
    },

    detail(item) {
      console.log("provider detail: ", item);
      this.$router.push({ name: "user-detail", params: { item } });
      // this.$route.push({ name: "detail-user", params: item });
    },
  },
  getUserInfo() {
    this.providersData.userInfo.get().then((snap) => {
      snap.forEach((doc) => {
        console.log("user", doc);
      });
    });
  },
  created() {
    this.searched = this.users;
  },
  mounted() {
    this.getProviderUser();
  },
};
</script>

<style>
.statusBtn {
  background: green;
  padding: 5px 10px;
}
.error {
  background: red;
}
</style>
