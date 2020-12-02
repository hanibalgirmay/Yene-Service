<template>
  <section>
    <h1>Reports</h1>
    <md-table
      class="table"
      v-model="reportList"
      md-card
      @md-selected="onSelect"
    >
      <md-table-toolbar>
        <h1 class="md-title">Selection Colors</h1>
      </md-table-toolbar>

      <md-table-row
        slot="md-table-row"
        slot-scope="{ item }"
        md-selectable="single"
      >
        <!-- <md-table-cell md-label="ID" md-sort-by="id" md-numeric>{{
            item.id
          }}</md-table-cell> -->
        <md-table-cell md-label="Report user" md-sort-by="service_name">{{
          item.ReportUserName
        }}</md-table-cell>
        <md-table-cell md-label="Reported User" md-sort-by="service_name">
          <md-chip class="red">
            {{ item.reportedUser }}
          </md-chip>
        </md-table-cell>
        <md-table-cell md-label="Icon">
          <md-avatar>
            <img :src="item.image" :alt="item" />
          </md-avatar>
        </md-table-cell>
        <md-table-cell md-label="Actions">
          <md-icon @click="showLayout = true">edit</md-icon>
        </md-table-cell>
      </md-table-row>
    </md-table>
  </section>
</template>

<script>
import { db } from "../firebase.config";

export default {
  data() {
    return {
      reportList: [],
      selected: null,
    };
  },
  methods: {
    reports() {
      db.collection("Reports")
        .get()
        .then((data) => {
          data.forEach((element) => {
            let t = {
              id: element.id,
              data: element.data().reported_description,
            };
            console.log("id-__:", element.data().user_reported);
            db.collection("Users")
              .doc(element.data().user_reported)
              .get()
              .then((user) => {
                console.log("reported data_", user.data());

                this.reportList.push({
                  ReportUserName: user.data().firstName,
                  reportedUser: user.data().lastName,
                  image: user.data().image,
                  message: t.data,
                });
                console.log("reported data_", this.reportList);
              });
          });
        })
        .catch((err) => console.log(err.message));
    },
  },
  created() {
    this.reports();
  },
};
</script>

<style>
.table {
  width: 100%;
}
.red {
  color: red;
  background-color: red !important;
}
</style>
