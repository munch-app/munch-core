<template>
  <div class="container-768 mtb-32">
    <text-auto class="h1" v-model="story.title" placeholder="Title"
               @change="story.$updated = new Date().getTime()"/>

    <div class="relative">
      <div v-if="loading" class="absolute-0 elevation-overlay flex-center">
        <h2>Loading...</h2>
      </div>

      <creator-item v-for="item in items" :key="item.itemId" :item="item" v-if="!item.$deleted"
                    @move-up="onMoveUp" @move-down="onMoveDown" @delete="onDelete" @change="onChange"/>
    </div>

    <creator-add-item class="mt-48" @on-type="onNewItem"/>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import Vue from 'vue';
  import TextAuto from "../../../components/core/TextAuto";

  import CreatorItem from "../../../components/creator/story/CreatorItem";
  import CreatorAddItem from "../../../components/creator/story/CreatorAddItem";

  export default {
    layout: 'creator',
    components: {CreatorAddItem, CreatorItem, TextAuto},
    head() {
      const title = this.story && this.story.title || 'Story'
      return {title: `${title} · ${this.creatorName}`}
    },
    computed: {
      ...mapGetters('creator', ['creatorName', 'creatorId']),
      storyId() {
        return this.story.storyId
      },
      creatorId() {
        return this.story.creatorId
      }
    },
    data() {
      return {
        items: [],
        loading: true,
      }
    },
    asyncData({$api, params: {storyId}, query, $error}) {
      if (query.new) {
        return {story: {storyId, title: query.title, type: query.title}}
      }

      return $api.get(`/creators/_/stories/${storyId}`).then(({data}) => {
        return {story: data}
      }).catch((err) => $error(err))
    },
    mounted() {
      this.$$autoUpdate = setInterval(this.update, 10000)
      this.appendItems()
    },
    beforeDestroy() {
      clearInterval(this.$$autoUpdate)
      this.update()
    },
    methods: {
      appendItems(next) {
        let params = {size: 30, sort: 'sort'}
        if (next) params['next.sort'] = next.sort

        return this.$api.get(`/creators/${this.creatorId}/stories/${this.storyId}/items`, {params})
          .then(({data, next}) => {
            this.items.push(...data)

            if (next) {
              appendItems(next)
            } else {
              this.loading = false
            }
          })
      },
      onNewItem(type) {
        const millis = new Date().getTime()
        const item = {
          type,
          sort: millis,
          storyId: this.story.storyId,
          itemId: `post-${millis}`,
          $updated: millis,
          $new: true
        }
        this.items.push(item)
      },
      onMoveUp(item) {
        const index = this.items.indexOf(item)

        // Highest Already
        if (index === 0) return
        if (index === 1) {
          item.sort = this.items[0].sort - 10000
        } else {
          const remain = this.items[index - 1].sort - this.items[index - 2].sort
          item.sort = this.items[index - 2].sort + (remain / 2)
        }

        this.items.sort((a, b) => a.sort - b.sort)
        item.$updated = new Date().getTime()
      },
      onMoveDown(item) {
        const index = this.items.indexOf(item)

        // Lowest Already
        if (index === this.items.length - 1) return
        if (index === this.items.length - 2) {
          item.sort = new Date().getTime()
        } else {
          const remain = this.items[index + 2].sort - this.items[index + 1].sort
          item.sort = this.items[index + 1].sort + (remain / 2)
        }

        this.items.sort((a, b) => a.sort - b.sort)
        item.$updated = new Date().getTime()
      },
      onDelete(item) {
        item.$updated = new Date().getTime()
        Vue.set(item, '$deleted', true)
      },
      onChange(item) {
        item.$updated = new Date().getTime()
      },
      update() {
        this.updateStory()
        this.updateItems()
      },
      updateStory() {
        const updated = this.story.$updated
        if (!updated) return

        return this.$api.patch(`/creators/${this.creatorId}/stories/${this.storyId}`, this.story).then(() => {
          console.log('Patched Story')

          if (this.story.$updated === updated) {
            Vue.delete(this.story, '$updated')
          }
        }).catch((err) => this.$store.dispatch('addError', err))
      },
      updateItems() {
        function deleteItem(item) {
          if (item.$new) return

          return this.$api.delete(`/creators/${this.creatorId}/stories/${this.storyId}/items/${item.itemId}`).then(() => {
            console.log('Deleted Item')
            Vue.delete(item, '$updated')
          }).catch((err) => this.$store.dispatch('addError', err))
        }

        function putItem(item) {
          const updated = item.$updated

          if (item.itemId && !item.$new) {
            return this.$api.patch(`/creators/${this.creatorId}/stories/${this.storyId}/items/${item.itemId}`, item).then(() => {
              console.log('Patched Item')

              if (item.$updated === updated) {
                Vue.delete(item, '$updated')
              }
            }).catch((err) => this.$store.dispatch('addError', err))
          } else {
            return this.$api.post(`/creators/${this.creatorId}/stories/${this.storyId}/items`, item).then(({data}) => {
              console.log('Posted Item')
              Vue.delete(item, '$new')
              item.itemId = data.itemId

              if (item.$updated === updated) {
                Vue.delete(item, '$updated')
              }
            }).catch((err) => this.$store.dispatch('addError', err))
          }
        }

        this.items.forEach(item => {
          if (item.$updated) {
            if (item.$deleted) {
              deleteItem.call(this, item)
            } else {
              putItem.call(this, item)
            }
          }
        })
      },
    }
  }
</script>

<style scoped lang="less">
</style>
