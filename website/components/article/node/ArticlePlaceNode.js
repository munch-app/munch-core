import {Node} from 'tiptap'
import ArticlePlace from './ArticlePlace.vue'

export default class Place extends Node {
  get name() {
    return 'place'
  }

  get schema() {
    return {
      attrs: {
        place: {
          default: null,
        }
      },
      group: 'block',
      draggable: true,

      parseDOM: [{tag: 'place',}],
      toDOM: node => ['place', node.attrs],
    }
  }

  commands({type}) {
    return (attrs) => (state, dispatch) => {
      const node = type.create(attrs)
      dispatch(state.tr.replaceSelectionWith(node))
    }
  }

  get view() {
    return ArticlePlace
  }
}
