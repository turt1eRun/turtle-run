// content/new/edit/page.tsx
'use client';

import { FC } from 'react';
import VideoPreview from '@/components/content/edit/VideoPreview';
import ThumbnailUploader from '@/components/content/edit/ThumbnailUploader';
import ContentEditor from '@/components/content/edit/ContentEditor';
import { useContentForm } from '@/hooks/useContentForm';

const EditPage: FC = () => {
  const {
    form,
    videoData,
    thumbnailFile,
    handleTitleChange,
    handleThumbnailUpload,
    handleImageUpload,
    updateBlocks,
    handleSubmit
  } = useContentForm();

  return (
      <div className="w-full mx-auto p-8">
        <h1 className="text-2xl text-black font-bold mb-6">컨텐츠 업로드</h1>

        <div className="w-full p-8 items-center">
          {/* 스텝 네비게이션 */}
          <div className="w-full grid grid-cols-3 mb-6">
            <div className="flex items-center font-semibold text-lg">
              <span className="text-[#c5ccde] mr-2">1.</span>
              <span className="text-[#c5ccde]">동영상 업로드</span>
            </div>
            <div className="flex items-center justify-center font-semibold text-lg">
              <span className="text-[#4285f4] mr-2">2.</span>
              <span className="text-[#4285f4]">정보 입력</span>
            </div>
            <div></div>
          </div>

          {/* 메인 컨텐츠 영역 */}
          <div className="grid grid-cols-2 gap-6">
            <VideoPreview
                videoUrl={videoData.uploadUrl}
                thumbnailUrl={thumbnailFile?.uploadUrl}
            />

            <div className="flex flex-col gap-4 h-[400px]">
              <input
                  type="text"
                  value={form.title}
                  onChange={(e) => handleTitleChange(e.target.value)}
                  placeholder="제목을 입력해주세요"
                  className="w-full p-4 border border-gray-200 rounded-lg text-gray-600"
              />

              <ThumbnailUploader
                  file={thumbnailFile}
                  onUpload={handleThumbnailUpload}
              />
            </div>
          </div>

          <ContentEditor
              blocks={form.blockRequests}
              onBlocksChange={updateBlocks}
              onImageUpload={handleImageUpload}
          />

          <div className="flex justify-end mt-8">
            <button
                onClick={handleSubmit}
                className="px-4 py-2 bg-[#4285f4] text-white rounded-full font-semibold text-sm hover:bg-[#4285f4]/90"
            >
              작성완료
            </button>
          </div>
        </div>
      </div>
  );
};

export default EditPage;