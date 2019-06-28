import {Node} from 'tiptap'
import ContentEditorAvatar from './ContentEditorAvatar.vue'

export default class Avatar extends Node {
  get name() {
    return 'avatar'
  }

  get schema() {
    return {
      attrs: {
        images: {
          default: null,
        },
        line1: {
          default: null,
        },
        line2: {
          default: null,
        }
      },
      group: 'block',
      inline: false,

      draggable: true,
      content: 'block*',
      selectable: true,

      parseDOM: [{tag: 'unknown-m-avatar',}],
      toDOM: node => ['unknown-m-avatar', node.attrs],
    }
  }

  commands({type}) {
    return (attrs) => (state, dispatch) => {
      const node = type.create(attrs)
      dispatch(state.tr.replaceSelectionWith(node))
    }
  }

  get view() {
    return ContentEditorAvatar
  }
}
