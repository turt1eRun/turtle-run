// types/content.ts
export interface Block {
  id: string;
  type: 'text' | 'image';
  content: string;
  orderNum: number;
  imageFile?: {
    id: number;
    uploadUrl: string;
    originalFileName: string;
  };
}
export interface UploadResponse {
  id: number;
  uploadUrl: string;
  originalFileName: string;
}

export interface ContentForm {
  title: string;
  videoFileId: number;
  thumbnailFileId: number | null;
  blockRequests: Block[];
}

export interface VideoData {
  id: number;
  uploadUrl: string;
  originalFileName: string;
}

// export interface ImageBlock {
//   id: number;
//   url: string;
//   fileName: string;
// }

export interface ContentEditorProps {
  blocks: Block[];
  onBlocksChange: (blocks: Block[]) => void;
  onImageUpload: (file: File) => Promise<void>;
}