export const state = () => ({
  menu: false
})

export const mutations = {
  showMenu(state, menu) {
    state.menu = menu
  },
  toggleMenu(state) {
    state.menu = !state.menu
  }
}
