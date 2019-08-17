<template>
  <div class="mt-24 mb-64">
    <h2>Requirements</h2>

    <div class="mt-24">
      <div class="Tag mtb-16 flex-between" v-for="tag in tags" :key="tag.tagId"
           @click="onClick(tag)">
        <h4>{{tag.name}}</h4>
        <div class="checkbox" :class="{selected: isSelected(tag)}"/>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'


  export default {
    name: "ProfileSearchPreferenceSection",
    data() {
      return {
        tags: [
          {
            tagId: 'abb22d3d-7d23-4677-b4ef-a3e09f2f9ada',
            name: 'Halal',
            type: 'Requirement'
          }, {
            tagId: 'fdf77b3b-8f90-419f-b711-dd25f97046fe',
            name: 'Vegetarian Options',
            type: 'Requirement'
          }
        ]
      }
    },
    computed: {
      ...mapGetters('user', ['searchPreference'])
    },
    methods: {
      onClick(tag) {
        const preference = JSON.parse(JSON.stringify(this.searchPreference))
        if (this.isSelected(tag)) {
          _.remove(preference.requirements, t => t.tagId === tag.tagId)
        } else {
          preference.requirements.push(tag)
        }

        this.$store.dispatch('user/updateSearchPreference', preference)
      },
      isSelected(tag) {
        const requirements = this.searchPreference.requirements
        return _.some(requirements, t => t.tagId === tag.tagId)
      }
    }
  }
</script>

<style scoped lang="less">
  .Tag {
    max-width: 300px;

  }
</style>
