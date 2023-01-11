export interface Menu {
  name: string;

  path: string;

  keyPath?: string[];

  icon?: string;

  key?: string; // default path

  // path contains param, auto assignment.
  paramPath?: string;

  disabled?: boolean;

  children?: Menu[];

  orderNo?: number;

  hideMenu?: boolean;

  group?: string;
}

export interface Nav {
  name: string;
  path?: string;
  key?: string;
  group?: string;
}
