<template>
  <div class="PlaceActionContainer index-elevation w-100 flex" v-if="isStaging">
    <div class="PlaceActionBar bg-white w-100 flex-justify-center">
      <div class="Action hover-pointer" @click="onMore">
        <simple-svg class="Icon wh-48px" fill="black" :filepath="require('~/assets/icon/place/action/more.svg')"/>
      </div>
      <div class="Action hover-pointer" @click="onAdd">
        <simple-svg class="Icon wh-48px" fill="black" :filepath="require('~/assets/icon/place/action/add.svg')"/>
      </div>
      <div class="Action hover-pointer" @click="onShare">
        <simple-svg class="Icon wh-48px" fill="black" :filepath="require('~/assets/icon/place/action/share.svg')"/>
      </div>
    </div>


    <div>
      <profile-collection-add-place :place="place" v-if="show.collection" @on-close="show.collection = false"/>

      <portal to="dialog-blank" v-if="show.more">
        <div class="MoreDialog bg-white border-4 border text-center text-ellipsis-1l">
          <div @click="onSuggest">
            Suggest Edits
          </div>
          <div @click="onAdd">
            Add To Collection
          </div>
          <div @click="onShare">
            Share
          </div>
          <hr>
          <div @click="show.more = false">
            Close
          </div>
        </div>
      </portal>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import ProfileCollectionAddPlace from "../profile/ProfileCollectionAddPlace";

  export default {
    name: "PlaceAction",
    components: {ProfileCollectionAddPlace},
    props: {
      place: {
        type: Object,
        require: true
      }
    },
    data() {
      return {
        show: {
          collection: false,
          more: false
        }
      }
    },
    computed: {
      ...mapGetters('user', ['isLoggedIn']),
      ...mapGetters(['isStaging', 'isMunchTeam']),
    },
    methods: {
      onMore() {
        this.show.more = true
      },
      onSuggest() {
        window.open(`/places/suggest?placeId=${this.place.placeId}`,'_blank');
      },
      onAdd() {
        this.show.more = false

        if (this.isLoggedIn) {
          this.show.collection = true
        } else {
          this.$store.commit('focus', 'Login')
        }
      },
      onShare() {
        this.show.more = false

        const url = window.location.href
        if (window.navigator && window.navigator.share) {
          window.navigator.share(url)

          this.$track.share('RIP', 'Native Share')
        } else {
          const dummy = document.createElement('input')
          document.body.appendChild(dummy)
          dummy.value = url
          dummy.select();
          document.execCommand('copy')
          document.body.removeChild(dummy)

          this.$store.dispatch('addMessage', {title: 'Copied Link!'})
          this.$track.share('RIP', 'Copied URL')
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .PlaceActionContainer {
    margin-top: 64px;

    position: sticky;
    bottom: 0;

    @media (min-width: 768px) {
      justify-content: flex-end;
    }
  }

  .PlaceActionBar {
    @media (max-width: 767.98px) {
      border-top: 1px solid rgba(0, 0, 0, .1);
    }

    @media (min-width: 768px) {
      margin-bottom: 24px;
      margin-right: 24px;

      width: calc(168px + 24px);
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
      border-radius: 28px;
      border: 1px solid rgba(0, 0, 0, .1);
    }
  }

  .Action {
    padding: 4px;
  }

  .MoreDialog {
    padding-top: 10px;

    hr {
      margin-top: 10px;
    }

    > div {
      padding-top: 10px;
      padding-bottom: 10px;

      font-weight: 600;
      font-size: 15px;
      line-height: 1.5;

      &:last-child {
        font-weight: 400;
      }

      &:hover {
        cursor: pointer;
      }
    }
  }
</style>
