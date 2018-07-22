<template>
  <div class="SearchSuggest">
    <div class="Results" v-if="items">
      <div class="Item" v-for="item in items" :key="item.key" @click="onClick(item)">
        <div class="Place" v-if="item.type === 'place'">
          <div class="Name">{{item.place.name}}</div>
        </div>
        <div class="Assumption" v-if="item.type === 'assumption'">
          <simple-svg class="Icon" fill="black" filepath="/img/search/search.svg"/>
          <div class="Token Border24 TagBg Small" v-for="token in item.assumption.tokens" :key="token.type + token.text">
            {{token.text}}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "SearchSuggest",
    props: {
      results: {
        required: true,
        twoWay: false
      }
    },
    computed: {
      items() {
        // Concat places & assumptions
        if (this.results) {
          let counter = 0;
          let items = []

          if (this.results.assumptions) {
            this.results.assumptions.forEach((assumption) => {
              items.push({
                type: 'assumption',
                key: 'assumption_' + counter,
                assumption: assumption
              })
              counter += 1
            });
          }

          if (this.results.places) {
            this.results.places.forEach((place) => {
              items.push({
                type: 'place',
                key: 'place_' + counter,
                place: place,
              })
              counter += 1
            });
          }

          return items.slice(0, 10)
        }
      }
    },
    methods: {
      onClick(item) {
        switch (item.type) {
          case 'place':
            this.$router.push({path: '/places/' + item.place.placeId})
            this.$emit('action')
            break
          case 'assumption':
            // TODO
            break
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .SearchSuggest {
    background: white;
    border-top: 1px solid #DDD;

    .Results {

      .Item {
        padding: 8px 12px;
        &:hover {
          cursor: pointer;
        }
      }
    }
  }

  .Place {

  }

  .Assumption {
    display: flex;
    justify-content: flex-start;
    align-items: center;

    .Icon {
      width: 20px;
      height: 20px;
      margin-right: 8px;
    }

    .Token {
      margin-right: 8px;
      padding: 5px 10px;

      font-size: 14px;
    }
  }
</style>
