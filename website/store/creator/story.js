export const state = () => ({
  story: null,
  items: [],
  next: null,
  loading: false,
})

export const getters = {
  story: (state) => state.story,
  storyTitle: (state) => state.story && state.story.title,
  storyId: (state) => state.story && state.story.storyId,
  creatorId: (state) => state.story && state.story.creatorId,
}

export const mutations = {
  set(state, story) {
    state.story = story
    state.items = []
    state.loading = true
  },

  appendItems(state, {items, next}) {
    state.items.push(...items)
    state.next = next
    state.loading = !!next
  }
}

function appendItems({commit, state}, story) {
  const {creatorId, storyId} = story

  if (!(state.story && state.story.storyId === storyId)) return
  if (!state.loading) return

  let params = {size: 30, sort: 'sort'}
  if (state.next) params['next.sort'] = next.sort

  return this.$api.get(`/creators/${creatorId}/stories/${storyId}/items`, {params})
    .then(({data, next}) => {
      commit('appendItems', {items: data, next})
      return appendItems.call(this, {commit, state}, story)
    })
}

export const actions = {

  /**
   * @param commit
   * @param state
   * @param story to start editing
   * @returns {{story: *}} for editing
   */
  start({commit, state}, story) {
    commit('set', story)
    appendItems.call(this, {commit, state}, story)
    return story
  },

  patch({commit, state}, story) {

  },

  postItem({commit, state}, item) {

  },

  patchItem({commit, state}, item) {

  },

  deleteItem({commit, state}, item) {

  },
}
