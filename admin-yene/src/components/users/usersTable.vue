<template>
  <div>
    <!-- dialog -->
    <div>
      <md-dialog id="msg" :md-active.sync="showLayout">
        <md-dialog-title>
          Message
        </md-dialog-title>
        <form>
          <md-chips class="md-primary" v-model="chips">
            <div class="md-helper-text">Default</div>
          </md-chips>
          <md-field :class="messageClass">
            <label>Required Field</label>
            <md-input v-model="required" required></md-input>
            <span class="md-error">There is an error</span>
          </md-field>

          <md-field :class="messageClass">
            <label>Textarea</label>
            <md-textarea v-model="textarea" required></md-textarea>
            <span class="md-helper-text">Helper text</span>
            <span class="md-error">There is an error</span>
          </md-field>
          <md-button>Send message</md-button>
        </form>
      </md-dialog>
    </div>

    <!-- end dialog -->
    <md-table
      class="table"
      v-model="searched"
      :md-sort.sync="currentSort"
      :md-sort-order.sync="currentSortOrder"
      md-card
      @md-selected="onSelect"
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
      <md-table-toolbar slot="md-table-alternate-header" slot-scope="{ count }">
        <div class="md-toolbar-section-start">
          {{ getAlternateLabel(count) }}
        </div>

        <div class="md-toolbar-section-end">
          <md-button class="md-icon-button" @click="showLayout = true">
            <md-icon>message</md-icon>
          </md-button>
          <md-menu
            style="margin-top:10px"
            md-size="medium"
            md-align-trigger
            class="md-icon-button"
          >
            <md-icon md-menu-trigger>more_vert</md-icon>
            <md-menu-content v-model="chips">
              <md-menu-item @click="RestPassword">Reset Password</md-menu-item>
              <md-menu-item @click="DisableAccount"
                >Disable account</md-menu-item
              >
              <md-menu-item @click="DeleteAccount">Delete Account</md-menu-item>
            </md-menu-content>
          </md-menu>
        </div>
      </md-table-toolbar>

      <md-table-empty-state
        md-label="No users found"
        :md-description="
          `No user found for this '${search}' query. Try a different search term or create a new user.`
        "
      >
        <md-button class="md-primary md-raised" @click="showLayout = true"
          >Create New User</md-button
        >
      </md-table-empty-state>

      <md-table-row
        slot-scope="{ item }"
        slot="md-table-row"
        md-selectable="multiple"
        md-auto-select
      >
        <!-- <md-table-cell md-label="ID" md-sort-by="id" md-numeric>{{
          user.id
        }}</md-table-cell> -->

        <md-table-cell md-label="ID" md-sort-by="id">{{ 1 }}</md-table-cell>
        <md-table-cell md-label="First Name" md-sort-by="firstName">{{
          item.firstName
        }}</md-table-cell>
        <md-table-cell md-label="Last Name" md-sort-by="lastName">{{
          item.lastName
        }}</md-table-cell>
        <md-table-cell md-label="Email" md-sort-by="email">{{
          item.email
        }}</md-table-cell>
        <md-table-cell md-label="Avatar" md-sort-by="avatar">
          <md-avatar>
            <img :src="item.avatar" alt="Avatar" />
          </md-avatar>
        </md-table-cell>
        <!-- <md-table-cell md-label="Action">
          <md-icon @click="action">menu</md-icon>
        </md-table-cell> -->
      </md-table-row>
    </md-table>
    <p>Selected:</p>
    {{ selected }}
  </div>
</template>

<script>
import { usersCollection } from "../../firebase.config";

const toLower = (text) => {
  return text.toString().toLowerCase();
};

const searchByName = (items, term) => {
  if (term) {
    return items.filter((item) =>
      toLower(item.firstName).includes(toLower(term))
    );
  }

  return items;
};

export default {
  name: "UsersTable",
  data: function() {
    return {
      currentSort: "firstName",
      currentSortOrder: "asc",
      search: null,
      userEmail: "",
      showLayout: false,
      i: 1,
      searched: [],
      chips: [],
      selected: [],
      usersData: [],
      // items: this.usersData,
    };
  },
  methods: {
    action() {
      alert("action clicked");
    },
    onSelect(items) {
      this.chips = [];
      this.selected = items;
      // this.chips = items;
      this.selected.forEach((element) => {
        this.chips.push(element.email);
      });
    },
    getAlternateLabel(count) {
      let plural = "";

      if (count > 1) {
        plural = "s";
      }

      return `${count} user${plural} selected`;
    },
    getClass: ({ id }) => ({
      "md-primary": id === 1,
      "md-accent": id === 3,
    }),
    DisableAccount() {
      this.chips.forEach((element) => {
        this.userEmail = element;
      });
      console.log(this.userEmail);
      console.log("Disabled Account: ", this.chips);
      // alert(this.chips)
    },
    RestPassword() {
      this.chips.forEach((element) => {
        this.userEmail = element;
        console.log(this.userEmail);
      });

      // auth
      //   .sendPasswordResetEmail(this.userEmail)
      //   .then((res) => alert("message sent", res))
      //   .catch((err) => console.log(err));
    },
    DeleteAccount() {
      this.chips.forEach((element) => {
        this.userEmail = element;
      });
      console.log(this.userEmail);
    },

    readUsers() {
      this.usersData = [];
      usersCollection
        .get()
        .then((querySnapShot) => {
          // querySnapShot.docs.map((doc) => {
          //   const documents = Object.assign(doc.data());
          //   console.log("Users: ", doc.data());
          //   this.usersData.push(documents);
          // });
          querySnapShot.forEach((doc) => {
            this.usersData.push({
              id: doc.id,
              firstName: doc.data().firstName,
              lastName: doc.data().lastName,
              email: doc.data().email,
              avatar: doc.data().image,
            });
            console.log(doc.id, " => ", doc.data());
          });
          console.log("Array users: ", this.usersData);
        })
        .catch((error) => {
          console.log("Error getting documents: ", error);
        });
    },
    newUser() {
      window.alert("Noop");
    },
    searchOnTable() {
      this.searched = searchByName(this.usersData, this.search);
    },
  },
  created() {
    this.searched = [];
    this.searched = this.usersData;
  },
  mounted() {
    this.readUsers();
    this.searched = this.usersData;
  },
};
</script>

<style>
.table {
  width: 100%;
}
#msg{
  
}
</style>
