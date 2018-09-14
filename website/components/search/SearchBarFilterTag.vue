<template>
  <div class="TagColumn">
    <div class="TagRow" v-for="tag in list" :key="tag" @click="toggle(tag)"
         :class="{Selected: isSelectedTag(tag), Loading: loading}">
      <div class="Name">
        {{tag}}
      </div>
      <div class="Control">
        <div v-if="!loading" class="Count">{{count(tag)}}</div>
        <div v-if="!loading || isSelectedTag(tag)" class="Checkbox"/>
        <beat-loader v-if="!isSelectedTag(tag) && loading" class="FlexCenter" color="#0A6284" size="6px"/>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import BeatLoader from 'vue-spinner/src/BeatLoader.vue'

  const types = {
    'cuisine': {
      list: ['Singaporean', 'Japanese', 'Italian', 'Thai', 'Chinese', 'Korean', 'Mexican', 'Western', 'Indian', 'Cantonese', 'English', 'Fusion', 'Asian', 'Hainanese', 'American', 'French', 'Hong Kong', 'Teochew', 'Taiwanese', 'Malaysian', 'Shanghainese', 'Indonesian', 'Vietnamese', 'European', 'Peranakan', 'Sze Chuan', 'Spanish', 'Middle Eastern', 'Modern European', 'Filipino', 'Turkish', 'Hakka', 'German', 'Mediterranean', 'Swiss', 'Hawaiian', 'Australian'],
    },
    'amenities': {
      list: ['Romantic', 'Supper', 'Brunch', 'Business Meal', 'Scenic View', 'Child-Friendly', 'Large Group', 'Vegetarian Options', 'Halal', 'Healthy', 'Alcohol', 'Vegetarian', 'Private Dining', 'Value For Money', 'Pet-Friendly', 'Live Music', 'Vegan', 'Vegan Options']
    },
    'establishments': {
      list: ['Hawker', 'Drinks', 'Bakery', 'Dessert', 'Snacks', 'Cafe', 'Bars & Pubs', 'Fast Food', 'BBQ', 'Buffet', 'Hotpot & Steamboat', 'High Tea', 'Fine Dining']
    }
  }

  function reduce(query, type) {
    let collector = []
    types[type].list.forEach(name => {
      if (query.filter.tag.positives.includes(name.toLowerCase())) {
        collector.push(name)
      }
    })
    return collector
  }

  export default {
    $$reduce: reduce,
    name: "SearchBarFilterTag",
    components: {BeatLoader},
    props: {
      type: {
        type: String,
        required: true
      }
    },
    data() {
      return {
        ...types[this.type]
      }
    },
    computed: {
      ...mapGetters('searchBar', ['isSelectedTag', 'loading']),
      tags() {
        return this.$store.state.searchBar.count.tags
      }
    },
    methods: {
      count(tag) {
        if (this.$store.state.searchBar.query.filter.tag.positives.length === 0) return ''
        if (this.isSelectedTag(tag)) return ''

        const count = this.tags[tag.toLowerCase()]
        if (count) {
          if (count >= 100) return '100+'
          else if (count <= 10) return `${count}`
          else return `${Math.round(count / 10) * 10}+`
        }
        return '0'
      },
      toggle(tag) {
        this.$store.dispatch('searchBar/tag', tag)
      }
    },
    watch: {
      tags(tags) {
        this.list.sort((a, b) => {
          const as = tags[a.toLowerCase()] || 0
          const bs = tags[b.toLowerCase()] || 0
          return bs - as
        })
      }
    }
  }
</script>

<style scoped lang="less">
  .TagColumn {
    overflow-y: scroll;

    @media (min-width: 768px) {
      // 104 Height of nav bar
      max-height: calc(90vh - 104px - 64px);
    }
  }

  .TagRow {
    display: flex;
    align-items: center;
    padding: 8px 0;

    &:hover {
      cursor: pointer;
    }

    .Name {
      padding-right: 8px;
      font-size: 16px;
      line-height: 24px;

      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;

      @media (min-width: 768px) {
        padding-right: 80px;
      }
    }

    .Control {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      flex-grow: 1;

      .Count {
        margin-right: 16px;
      }

      .Checkbox {
        height: 24px;
        width: 24px;
        position: relative;
        display: inline-block;

        &::before, &::after {
          position: absolute;
          content: "";
          display: inline-block;
        }

        &::before {
          margin: 1px 0;
          height: 22px;
          width: 22px;
          border-radius: 3px;
          border: 2px solid rgba(0, 0, 0, 0.65);
        }

        &::after {
          height: 7px;
          width: 11px;
          border-left: 2px solid;
          border-bottom: 2px solid;

          transform: rotate(-45deg);

          left: 5px;
          top: 7px;
        }

        &::after {
          content: none;
        }
      }
    }
  }

  @Primary500: #F05F3B;

  .TagRow.Selected {
    .Checkbox::after {
      content: "";
      border-color: white;
    }

    .Checkbox::before {
      border-color: @Primary500;
      background-color: @Primary500;
    }

    .Checkbox::before {
      border-color: @Primary500;
      background-color: @Primary500;
    }
  }
</style>
