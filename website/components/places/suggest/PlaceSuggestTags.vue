<template>
  <div class="input-text">
    <label>{{label}}</label>
    <no-ssr>
      <vue-tags-input placeholder="Add Tag" v-model="tag" :tags="value" @tags-changed="newTags => onChange(newTags)"/>
    </no-ssr>
  </div>
</template>

<script>

  let components = {};

  if (process.client) {
    components = {'VueTagsInput': require("@johmun/vue-tags-input").default};
  }

  export default {
    name: "PlaceSuggestTags",
    components: components,
    data() {
      return {
        tag: ""
      }
    },
    props: {
      label: {
        type: String,
        required: true
      },
      value: {
        type: [Array],
      },
    },
    methods: {
      onChange(newTags) {
        this.$emit('input', newTags)
      }
    }
  }
</script>

<style lang="less">
  .vue-tags-input {
    background: white;
    max-width: 100% !important;
  }

  .vue-tags-input {
    background: transparent;
    color: #b7c4c9;
  }

  .vue-tags-input {
    .ti-input {
      .ti-tags {
        .ti-tag.ti-valid.ti-deletion-mark {
          background-color: #AC2D0E;
          color: white;
        }

        .ti-tag.ti-valid {
          background-color: #F05F3B;
          color: white;
        }
      }
    }
  }
</style>
