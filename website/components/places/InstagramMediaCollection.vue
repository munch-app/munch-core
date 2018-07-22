<template>
  <div>
    <b-row>
      <b-col class="InstagramCol" cols="6" md="3" v-for="media in display" :key="media.mediaId">
        <instagram-media-card :media="media"/>
      </b-col>
    </b-row>

    <div class="Controls">
      <div class="Left Elevation1 Border24" :class="{'Primary200Bg': hasLeft, 'Secondary050Bg': !hasLeft}"
           @click="onLeft">
        <simple-svg class="Icon" fill="white" filepath="/img/places/caret_left.svg"/>
      </div>
      <div class="Page Border24">{{page}}</div>
      <div class="Right Elevation1 Border24" :class="{'Primary200Bg': hasRight, 'Secondary050Bg': !hasRight}"
           @click="onRight">
        <simple-svg class="Icon" fill="white" filepath="/img/places/caret_right.svg"/>
      </div>
    </div>
  </div>
</template>

<script>
  import InstagramMediaCard from "./InstagramMediaCard";

  export default {
    name: "InstagramMediaCollection",
    components: {InstagramMediaCard},
    props: {
      placeId: {
        required: true,
        type: String
      },
      medias: {
        required: true,
        type: Array
      }
    },
    data() {
      return {
        per: 4,
        index: 0,
        list: this.medias,
        display: this.medias.slice(0, 4),hasLeft: false,
        hasRight: true
      }
    },
    computed: {
      page() {
        return (this.index / this.per) + 1
      }
    },
    methods: {
      updateDisplay() {
        this.display = this.list.slice(this.index, this.index + this.per)
        this.hasLeft = this.index !== 0
        this.hasRight = (this.list.length - (this.index + this.per)) >= 0
      },
      onLeft() {
        if (this.hasLeft) {
          this.index -= this.per
          this.updateDisplay()
        }
      },
      onRight() {
        if (this.hasRight) {
          this.index += this.per
          this.updateDisplay()
        }

        if ((this.list.length - (this.index + this.per)) < this.per) {
          // Load more
          let nextPlaceSort = this.list[this.list.length - 1].placeSort
          this.$axios.$get('/api/places/' + this.placeId + '/partners/instagram/medias?next.placeSort=' + nextPlaceSort, {progress: false}
          ).then(({data}) => {
            [].push.apply(this.list, data);
            this.updateDisplay()
          })
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .InstagramCol {
    margin-top: 15px;
    margin-bottom: 15px;
  }

  .Controls {
    display: flex;
    justify-content: flex-end;
    align-items: flex-end;

    .Page, .Left, .Right {
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;

      width: 40px;
      height: 40px;
      margin-left: 8px;

      .Icon {
        width: 20px;
        height: 20px;
      }
    }
  }
</style>
