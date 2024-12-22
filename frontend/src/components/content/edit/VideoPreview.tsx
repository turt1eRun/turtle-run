// components/content/edit/VideoPreview.tsx
import {FC} from "react";

interface VideoPreviewProps {
  videoUrl: string;
  thumbnailUrl?: string;
}

const VideoPreview: FC<VideoPreviewProps> = ({ videoUrl, thumbnailUrl }) => {
  return (
      <div className="bg-white rounded-lg border border-gray-200 h-[400px] overflow-hidden">
        <video
            src={videoUrl}
            controls
            poster={thumbnailUrl}
            className="w-full h-full object-contain"
        />
      </div>
  );
};

export default VideoPreview;