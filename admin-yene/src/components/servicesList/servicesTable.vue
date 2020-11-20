<template>
  <div class="md-layout">
    <div class="md-layout-item">
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
      <md-table
        class="table"
        v-model="services"
        md-card
        @md-selected="onSelect"
      >
        <md-table-toolbar>
          <h1 class="md-title">Selection Colors</h1>
        </md-table-toolbar>

        <md-table-row
          slot="md-table-row"
          slot-scope="{ item }"
          :class="getClass(item)"
          md-selectable="single"
        >
          <!-- <md-table-cell md-label="ID" md-sort-by="id" md-numeric>{{
            item.id
          }}</md-table-cell> -->
          <md-table-cell md-label="Service Name" md-sort-by="service_name">{{
            item.service_name
          }}</md-table-cell>
          <md-table-cell md-label="Icon">
            <md-avatar>
              <img :src="item.service_icon" :alt="item.service_name" />
            </md-avatar>
          </md-table-cell>
          <md-table-cell md-label="Image">
            <md-avatar>
              <img :src="item.img" :alt="item.service_name" />
            </md-avatar>
          </md-table-cell>
          <md-table-cell md-label="Actions">
            <md-icon @click="showLayout = true">edit</md-icon>
          </md-table-cell>
        </md-table-row>
      </md-table>

      <p>Selected:</p>
      {{ selected }}
    </div>
  </div>
</template>

<script>
import { db } from "../../firebase.config";

export default {
  data() {
    return {
      showLayout: false,
      selected: {},
      people: [
        {
          id: 1,
          name: "Shawna Dubbin",
          email: "sdubbin0@geocities.com",
          gender: "Male",
          title: "Assistant Media Planner",
        },
        {
          id: 2,
          name: "Odette Demageard",
          email: "odemageard1@spotify.com",
          gender: "Female",
          title: "Account Coordinator",
        },
        {
          id: 3,
          name: "Lonnie Izkovitz",
          email: "lizkovitz3@youtu.be",
          gender: "Female",
          title: "Operator",
        },
        {
          id: 4,
          name: "Thatcher Stave",
          email: "tstave4@reference.com",
          gender: "Male",
          title: "Software Test Engineer III",
        },
        {
          id: 5,
          name: "Clarinda Marieton",
          email: "cmarietonh@theatlantic.com",
          gender: "Female",
          title: "Paralegal",
        },
      ],
      services: [],
      servicesList: [],
    };
  },
  methods: {
    getClass: ({ id }) => ({
      "md-primary": id === 2,
      "md-accent": id === 3,
    }),
    onSelect(item) {
      this.selected = item;
      //   this.$router.replace({ name: "/dash" });
      this.$router.replace({
        name: "serviceCatgory",
        params: { serviceName: item.service_name },
      });
    },
    getService() {
      db.collection("Services")
        .get()
        .then((snapshot) => {
          let s = snapshot.docs.length;
          if (s > 0) {
            snapshot.forEach((docs) => {
              this.services.push(docs.data());
            });
          }
        })
        .catch((err) => console.error(err));
    },
    getSubService() {
      db.collection("Services_List")
        .get()
        .then((snapshot) => {
          let s = snapshot.docs.length;
          if (s > 0) {
            snapshot.forEach((docs) => {
              let c = {
                maker: docs.data().name,
                category: docs.data().category,
              };
              this.servicesList.push(c);
            });
            // const t = this.services.service_name === this.servicesList.category;
            // if(t){
            //     numberCount =
            // }
          }
        })
        .catch((err) => console.error(err));
    },
  },
  watch: {},
  created() {
    this.getService();
  },
};
</script>

<style scoped>
.table {
  width: 100%;
}
</style>
