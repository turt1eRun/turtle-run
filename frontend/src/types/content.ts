// types/content.ts

// 백엔드 DTO와 매칭되는 타입
export interface BlockRequest {
  text: string | null;
  descFileId: number | null;
  orderNum: number;
}

// 블록 기본 타입
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

// 파일 업로드 응답 타입
export interface UploadResponse {
  id: number;
  uploadUrl: string;
  originalFileName: string;
}

// 컨텐츠 폼 데이터 타입
export interface ContentForm {
  title: string;
  videoFileId: number;
  thumbnailFileId: number | null;
  blockRequests: BlockRequest[]; // BlockRequest로 변경
}

// 비디오 데이터 타입
export interface VideoData {
  id: number;
  uploadUrl: string;
  originalFileName: string;
}

// ContentEditor 컴포넌트 props 타입
export interface ContentEditorProps {
  blocks: Block[];
  onBlocksChange: (blocks: Block[]) => void;
  onImageUpload: (file: File) => Promise<void>;
}