<template>
  <div>
    <graph-pie
      :width="500"
      :height="500"
      :padding-top="75"
      :padding-right="75"
      :padding-bottom="75"
      :padding-left="75"
      :values="values"
      :names="names"
      :active-index="[0, 2]"
      :active-event="'click'"
      :show-text-type="'outside'"
      :data-format="dataFormat"
      :shape="'donut'"
      :show-total-value="true"
    >
      <note :text="'Donut Chart'"></note>
      <legends :names="names"></legends>
      <tooltip :names="names"></tooltip>
    </graph-pie>
  </div>
</template>

<script>
export default {
  data() {
    return {
      values: [10, 5, 5, 5],
      names: ["Apple", "Google", "MS", "Facebook"],
    };
  },
  methods: {
    dataFormat: function(a, b) {
      if (b) return b + "%";
      return a;
    },
  },
};
</script>

<style></style>
