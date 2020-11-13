<template>
  <section>
    <md-table v-model="reviewsData" md-card>
      <md-table-toolbar>
        <h1 class="md-title">Users Reviews</h1>
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
        <md-table-cell md-label="Comment">{{ item.content }}</md-table-cell>
        <md-table-cell md-label="Rate">{{ item.rate }}</md-table-cell>
        <md-table-cell md-label="uId">{{ item.appointementID }}</md-table-cell>
        <md-table-cell md-label="Job Title">{{
          item.service_provider_id
        }}</md-table-cell>
      </md-table-row>
    </md-table>
  </section>
</template>

<script>
import { reviewsCollection } from "../firebase.config";

export default {
  name: "Reviews",
  data() {
    return {
      selected: {},
      reviewsData: [],
    };
  },
  methods: {
    getClass: ({ id }) => ({
      "md-primary": id === 2,
      "md-accent": id === 3,
    }),
    onSelect(item) {
      this.selected = item;
    },
    getReviews() {
      reviewsCollection
        .get()
        .then((snap) => {
          snap.forEach((elemt) => {
            console.log("d", elemt.data());

            this.reviewsData.push(elemt.data());
          });
        })
        .catch((error) => console.error(error));
    },
  },
  created() {
    this.getReviews();
    console.log("====================================");
    console.log("review:", this.getReviews());
    console.log("====================================");
  },
};
</script>

<style scoped>
.md-content {
  width: 100%;
}
</style>
