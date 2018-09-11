<template>
  <div class="TagColumn">
    <div class="TagRow" v-for="tag in display" :key="tag" @click="toggle(tag)"
         :class="{Selected: isSelected(tag)}">
      <div class="Name">
        {{tag}}
      </div>
      <div class="Control">
        <div class="Count">{{count(tag)}}</div>
        <div class="Checkbox"/>
      </div>
    </div>
  </div>
</template>

<script>
  const types = {
    'cuisine': {
      list: ['Singaporean', 'Japanese', 'Italian', 'Thai', 'Chinese', 'Korean', 'Mexican', 'Western', 'Indian', 'Cantonese', 'English', 'Fusion', 'Asian', 'Hainanese', 'American', 'French', 'Hong Kong', 'Teochew', 'Taiwanese', 'Malaysian', 'Shanghainese', 'Indonesian', 'Vietnamese', 'European', 'Peranakan', 'Sze Chuan', 'Spanish', 'Middle Eastern', 'Modern European', 'Filipino', 'Turkish', 'Hakka', 'German', 'Mediterranean', 'Swiss', 'Hawaiian', 'Australian'],
    },
    'amenities': {
      list: ['Romantic', 'Supper', 'Brunch', 'Business Meal', 'Scenic View', 'Child-Friendly', 'Large Group', 'Vegetarian Options', 'Halal', 'Healthy', 'Alcohol', 'Vegetarian', 'Private Dining', 'Budget', 'Pet-Friendly', 'Live Music', 'Vegan', 'Vegan Options']
    },
    'establishments': {
      list: ['Hawker', 'Drinks', 'Bakery', 'Dessert', 'Snacks', 'Cafe', 'Bars & Pubs', 'Fast Food', 'BBQ', 'Buffet', 'Hotpot & Steamboat', 'High Tea', 'Fine Dining']
    }
  }

  export default {
    name: "SearchBarFilterTag",
    props: {
      type: {
        type: String,
        required: true
      }
    },
    data() {
      return {
        display: types[this.type].list,
        ...types[this.type]
      }
    },
    methods: {
      isSelected(tag) {
        return this.$store.getters['searchBar/isSelectedTag'](tag)
      },
      count(tag) {
        return '100'
      },
      toggle(tag) {
        this.$store.commit('searchBar/toggleTag', tag)
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

  @Primary600: #EE4C23;

  .TagRow.Selected {

    .Checkbox::after {
      content: "";
      border-color: @Primary600;
    }

    .Checkbox::before {
      border-color: @Primary600;
    }
  }
</style>
