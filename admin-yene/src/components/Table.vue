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
    <md-table id="table" v-model="people" md-card @md-selected="onSelect">
      <md-table-toolbar>
        <h1 class="md-title">With auto select and alternate headers</h1>
      </md-table-toolbar>

      <md-table-toolbar slot="md-table-alternate-header" slot-scope="{ count }">
        <div class="md-toolbar-section-start">
          {{ getAlternateLabel(count) }}
        </div>

        <div class="md-toolbar-section-end">
          <md-button class="md-icon-button" @click="showLayout = true">
            <md-icon>chat_bubble</md-icon>
          </md-button>
        </div>
      </md-table-toolbar>

      <md-table-row
        slot="md-table-row"
        slot-scope="{ item }"
        :md-disabled="item.name.includes('Stave')"
        md-selectable="multiple"
        md-auto-select
      >
        <md-table-cell md-label="Name" md-sort-by="name">{{
          item.name
        }}</md-table-cell>
        <md-table-cell md-label="Email" md-sort-by="email">{{
          item.email
        }}</md-table-cell>
        <md-table-cell md-label="Gender" md-sort-by="gender">{{
          item.gender
        }}</md-table-cell>
        <md-table-cell md-label="Job Title" md-sort-by="title">{{
          item.title
        }}</md-table-cell>
      </md-table-row>
    </md-table>

    <p>Selected:</p>
    {{ selected }}
  </div>
</template>

<script>
export default {
  name: "Table",
  data() {
    return {
      selected: [],
      showLayout: false,
      chips: [],
      people: [
        {
          name: "Shawna Dubbin",
          email: "sdubbin0@geocities.com",
          gender: "Male",
          title: "Assistant Media Planner",
        },
        {
          name: "Odette Demageard",
          email: "odemageard1@spotify.com",
          gender: "Female",
          title: "Account Coordinator",
        },
        {
          name: "Lonnie Izkovitz",
          email: "lizkovitz3@youtu.be",
          gender: "Female",
          title: "Operator",
        },
        {
          name: "Thatcher Stave",
          email: "tstave4@reference.com",
          gender: "Male",
          title: "Software Test Engineer III",
        },
        {
          name: "Clarinda Marieton",
          email: "cmarietonh@theatlantic.com",
          gender: "Female",
          title: "Paralegal",
        },
      ],
    };
  },
  methods: {
    onSelect(items) {
      this.chips = [];
      this.selected = items;
      // this.chips = items;

      this.selected.forEach((element) => {
        this.chips.push(element.name);
      });
    },
    getAlternateLabel(count) {
      let plural = "";

      if (count > 1) {
        plural = "s";
      }

      return `${count} user${plural} selected`;
    },
  },
};
</script>

<style>
#table,
.md-table {
  width: 100%;
}
#msg form {
  padding: 2em;
  margin-left: 1em;
  width: 400px;
}
#msg {
  position: fixed;
  /* top: 0; */
  /* left: 0; */
  padding: 2em;
  right: 0;
  bottom: 0;
  align-items: center;
  justify-content: center;
  display: flex;
  transition-duration: 0.2s;
  /* z-index: 11; */
}
</style>
