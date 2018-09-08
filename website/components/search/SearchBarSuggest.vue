<template>
  <div class="SearchSuggest NoSelect" v-if="items">
    <div class="Results">

      <div class="Item" :class="{'OnPosition': position === item.position}" v-for="item in items"
           :key="item.position" @click="onClick(item)">
        <simple-svg class="Icon" fill="black" :filepath="`/img/search/${item.type}.svg`"/>

        <div class="Content">
          <div class="Place Text" v-if="item.type === 'place'">
            <span class="Name">{{item.place.name}}</span>
            <span class="Location">, {{item.place.location.neighbourhood}}</span>
          </div>

          <div class="Assumption Text" v-if="item.type === 'assumption'">
            <div class="Token Border24 TagBg" v-if="item.type === 'assumption'"
                 v-for="token in item.assumption.tokens" :key="token.type + token.text">
              <span class="TokenText">{{token.text}}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "SearchBarSuggest",
    props: {
      suggestions: {
        required: true
      }
    },
    data() {
      return {
        position: 0
      }
    },
    computed: {
      items() {
        if (this.suggestions) {
          let counter = 0;
          let items = []

          if (this.suggestions.assumptions) {
            this.suggestions.assumptions.forEach((assumption) => {
              items.push({
                type: 'assumption',
                position: counter,
                assumption: assumption
              })
              counter += 1
            });
          }

          if (this.suggestions.places) {
            this.suggestions.places.forEach((place) => {
              items.push({
                type: 'place',
                position: counter,
                place: place,
              })
              counter += 1
            });
          }
          if (items.length > 0) {
            return items.slice(0, 10)
          }
        }
      }
    },
    mounted() {
      window.addEventListener('keyup', this.keyUpListener);
    },
    methods: {
      onClick(item) {
        switch (item.type) {
          case 'place':
            this.$router.push({path: '/places/' + item.place.placeId})
            this.$emit('onItem', item)
            break

          case 'assumption':
            this.$store.commit('search/update', item.assumption.searchQuery)
            this.$emit('onItem', item)
            this.$router.push({path: '/search'})
            break
        }
      },
      keyUpListener(evt) {
        switch (evt.keyCode) {
          case 38: // Up
            if (this.position === 0) break
            this.position -= 1
            break

          case 40: // Down
            if (this.position === this.items.length - 1) break
            this.position += 1
            break

          case 13: // Enter
            this.onClick(this.items[this.position])
            break
        }
      },
    }
  }
</script>

<style scoped lang="less">
  .SearchSuggest {
    background: white;
    border-top: 1px solid #DDD;
  }

  .Item {
    height: 42px;
    padding: 8px 14px;
    clear: both;

    .Icon {
      float: left;
      width: 24px;
      height: 24px;
      margin: 1px 14px 1px 0;
    }

    .Content {
      height: 26px;
      width: 100%;
    }

    &:hover {
      cursor: pointer;
      background: #BEC9D0;
    }

    &.OnPosition {
      background: #BEC9D0;
    }
  }

  .Place {
    height: 26px;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;

    span.Name {
      font-weight: 600;
    }

    span.Location {

    }
  }

  .Assumption {
    display: flex;
    justify-content: flex-start;
    align-items: center;

    .Token {
      height: 26px;
      margin-right: 8px;

      .TokenText {
        font-size: 14px;
        font-weight: 500;
        margin: auto 12px;
      }
    }
  }
</style>
