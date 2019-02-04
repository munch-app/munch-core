export const state = () => ({
  story: null,
  items: null,
})

export const getters = {
  story: (state) => state.story,
  storyTitle: (state) => state.story && state.story.title,
  creatorId: (state) => state.story && state.story.creatorId,
  storyId: (state) => state.story && state.story.storyId,
}

export const mutations = {
  set(state, story) {
    state.story = story
    state.items = null
  },
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
